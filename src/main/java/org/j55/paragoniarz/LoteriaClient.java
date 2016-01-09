package org.j55.paragoniarz;

import com.sun.istack.internal.NotNull;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.j55.paragoniarz.core.ClientException;
import org.j55.paragoniarz.core.Properties;
import org.j55.paragoniarz.processing.Receipt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.j55.paragoniarz.Constants.*;

/**
 * @author johnnyFiftyFive
 */
public class LoteriaClient {
    private static final String ADDRESS = "https://loteriaparagonowa.gov.pl";

    private String cookies = "";
    private HttpClient httpclient;

    public LoteriaClient() {
        httpclient = HttpClients.createDefault();
    }


    public void pushReceipt(Receipt receipt) throws ClientException {
        HttpGet get = new HttpGet(ADDRESS);
        try {
            String token = extractToken(executeRequest(get)
                    .getEntity()
                    .getContent());
            HttpPost post = prepareLoginReq(token);
            executeRequest(post);
            get.reset();

            HttpResponse afterLogin = executeRequest(get);
            Document doc = Jsoup.parse(afterLogin.getEntity().getContent(), "UTF-8", ADDRESS);
            token = extractToken(doc);
            String operation = extractCaptchaOperation(doc);
            get.reset();

            String captcha = solve(operation);
            HttpPost captchaReq = prepareCaptchaReq(token, captcha);
            executeRequest(captchaReq);
            captchaReq.reset();

            HttpGet captchaUpdateReq = prepareCaptchaUpdateReq(token);
            executeRequest(captchaUpdateReq);
            captchaUpdateReq.reset();

            HttpPost addReceiptReq = prepareReceiptReq(receipt, token, captcha);
            executeRequest(addReceiptReq);
            addReceiptReq.reset();

        } catch (IOException e) {
            throw new ClientException("Communication error", e);
        }

    }

    private HttpResponse executeRequest(HttpRequestBase req) throws IOException, ClientException {
        HttpResponse resp = httpclient.execute(req);
        setCookies(resp.getFirstHeader("set-cookie").toString());
        int status = resp.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new ClientException("Something is not right with the response, status=" + status);
        }
        return resp;
    }


    private HttpPost prepareCaptchaReq(String token, String captcha) {
        HttpPost post = new HttpPost(ADDRESS + "/validation/captcha");
        post.setHeaders(createHeaders(token));

        List<NameValuePair> pairs = new ArrayList<>(1);
        pairs.add(new BasicNameValuePair("captcha", captcha));
        setEntity(post, pairs);
        return post;
    }


    private HttpGet prepareCaptchaUpdateReq(String token) {
        HttpGet get = new HttpGet(ADDRESS + "/validation/captcha?update=1");
        get.setHeaders(createHeaders(token));
        return get;
    }

    private HttpPost prepareLoginReq(@NotNull String token) {
        HttpPost post = new HttpPost(ADDRESS + "/auth/login");
        post.setHeaders(createHeaders(token));

        List<NameValuePair> pairs = new ArrayList<>(3);
        pairs.add(new BasicNameValuePair("_token", token));
        pairs.add(new BasicNameValuePair("email", Properties.get(Properties.USER)));
        pairs.add(new BasicNameValuePair("password", Properties.get(Properties.PASS)));
        setEntity(post, pairs);

        return post;
    }

    private HttpPost prepareReceiptReq(Receipt receipt, String token, @NotNull String captcha) throws ClientException {
        if (captcha.isEmpty()) {
            throw new ClientException("Captcha is empty");
        }
        HttpPost post = new HttpPost(ADDRESS + "/paragon/stworz");
        post.setHeaders(createHeaders(token));

        List<NameValuePair> pairs = new ArrayList<>(15);
        pairs.add(new BasicNameValuePair(CASH_ID, receipt.getCashId()));
        pairs.add(new BasicNameValuePair(TAX_NUMBER, "6271159856"));
        pairs.add(new BasicNameValuePair(YEAR, "2016"));
        pairs.add(new BasicNameValuePair(MONTH, "01"));
        pairs.add(new BasicNameValuePair(DAY, "07"));
        pairs.add(new BasicNameValuePair(PRINT_NUMBER, "001199"));
        pairs.add(new BasicNameValuePair(TOTAL_ZL, "17"));
        pairs.add(new BasicNameValuePair(TOTAL_GR, "00"));
        pairs.add(new BasicNameValuePair(BUSINESS_TYPE, ""));
        pairs.add(new BasicNameValuePair(CAPTCHA, captcha));
        pairs.add(new BasicNameValuePair(DATA_AGGR, "true"));
        pairs.add(new BasicNameValuePair(IMAGE_AGGR, "false"));
        pairs.add(new BasicNameValuePair(EMAIL, Properties.get(Properties.USER)));
        pairs.add(new BasicNameValuePair(PHONE_NUMBER, Properties.get(Properties.PHONE)));
        setEntity(post, pairs);

        return post;
    }


    private Header[] createHeaders(String token) {
        Header[] headers = {
                new BasicHeader("x-csrf-token", token),
                new BasicHeader("cookie", cookies),
                new BasicHeader("content-type", "application/x-www-form-urlencoded"),
                new BasicHeader("origin", ADDRESS),
                new BasicHeader("referer", ADDRESS)};
        return headers;
    }

    private String extractToken(InputStream is) throws IOException {
        Document doc = Jsoup.parse(is, "UTF-8", ADDRESS);
        return extractToken(doc);
    }

    private String extractToken(Document doc) {
        Element metaToken = doc.head().getElementsByAttributeValue("name", "csrf-token").get(0);
        return metaToken.attr("content");
    }

    private String extractCaptchaOperation(Document doc) {
        Element operationEl = doc.getElementById("captcha-operation");
        return operationEl.text();
    }

    private String solve(String operation) {
        String[] tokens = operation.split("\\+");
        Optional<String> result = Optional.of(Stream.of(tokens).map(Integer::parseInt).reduce(0, (sum, t) -> sum + t).toString());
        return result.orElse("");
    }

    private void setCookies(String cookies) {
        this.cookies = cookies;
    }

    private void setEntity(HttpPost post, List<NameValuePair> pairs) {
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // will not happen
        }
    }
}
