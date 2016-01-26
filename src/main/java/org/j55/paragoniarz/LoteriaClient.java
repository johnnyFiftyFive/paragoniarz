package org.j55.paragoniarz;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.jsoup.select.Elements;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.j55.paragoniarz.Constants.*;

/**
 * @author johnnyFiftyFive
 */
public class LoteriaClient {
    private static final String ADDRESS = "https://loteriaparagonowa.gov.pl/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.48 Safari/537.36 Vivaldi/1.0.365.3";
    private String cookies = "";
    private HttpClient httpclient;
    private ObjectMapper mapper;

    public LoteriaClient() {
        httpclient = HttpClients.createDefault();
        mapper = new ObjectMapper();
    }


    public void pushReceipt(Receipt receipt) throws ClientException {
        HttpGet get = new HttpGet(ADDRESS);
        try {
            HttpResponse mainPageResp = executeRequest(get);
            Document mainPage = Jsoup.parse(mainPageResp.getEntity().getContent(), "UTF-8", ADDRESS);
            String token = extractToken(mainPage);
            if (!isLoggedIn(mainPage)) {
                HttpPost post = prepareLoginReq(token);
                executeRequest(post);
                post.reset();
            }
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
            HttpResponse addReceiptResp = executeRequest(addReceiptReq);
            String internalNumber = extractInternalNumber(addReceiptResp.getEntity().getContent());
            receipt.setInternalNumber(internalNumber);
            addReceiptReq.reset();
        } catch (IOException e) {
            throw new ClientException("Communication error", e);
        }
    }

    private String extractInternalNumber(InputStream json) throws IOException {
        Message msg = mapper.readValue(json, Message.class);
        Document doc = Jsoup.parse(msg.getMessage());
        Elements elements = doc.getElementsByClass("recipe-number");
        if (!elements.isEmpty()) {
            return elements.first().text();
        }
        return null;
    }

    private boolean isLoggedIn(Document mainPage) {
        return mainPage.toString().contains("Wyloguj");
    }

    private HttpResponse executeRequest(HttpRequestBase req) throws IOException, ClientException {
        HttpResponse resp = httpclient.execute(req);
        setCookies(resp.getFirstHeader("set-cookie").getValue());
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

    private HttpPost prepareLoginReq(String token) {
        HttpPost post = new HttpPost(ADDRESS + "/auth/login");
        post.setHeaders(createHeaders(token));

        List<NameValuePair> pairs = new ArrayList<>(3);
        pairs.add(new BasicNameValuePair("_token", token));
        pairs.add(new BasicNameValuePair("email", Properties.get(Properties.USER)));
        pairs.add(new BasicNameValuePair("password", Properties.get(Properties.PASS)));
        setEntity(post, pairs);

        return post;
    }

    private HttpPost prepareReceiptReq(Receipt receipt, String token, String captcha) throws ClientException {
        if (captcha.isEmpty()) {
            throw new ClientException("Captcha is empty");
        }
        HttpPost post = new HttpPost(ADDRESS + "/paragon/stworz");
        post.setHeaders(createHeaders(token));

        List<NameValuePair> pairs = new ArrayList<>(15);
        pairs.add(new BasicNameValuePair(CASH_ID, receipt.getCashId()));
        pairs.add(new BasicNameValuePair(TAX_NUMBER, receipt.getTaxNumber()));
        pairs.addAll(addDate(receipt.getTransactionDate()));
        pairs.addAll(addTotal(receipt.getTotal()));
        pairs.add(new BasicNameValuePair(RECEIPT_NUMBER, receipt.getReceiptNumber()));
        pairs.add(new BasicNameValuePair(BUSINESS_TYPE, ""));
        pairs.add(new BasicNameValuePair(CAPTCHA, captcha));
        pairs.add(new BasicNameValuePair(DATA_AGGR, "true"));
        pairs.add(new BasicNameValuePair(IMAGE_AGGR, "false"));
        pairs.add(new BasicNameValuePair(EMAIL, Properties.get(Properties.USER)));
        pairs.add(new BasicNameValuePair(PHONE_NUMBER, Properties.get(Properties.PHONE)));
        setEntity(post, pairs);

        return post;
    }

    private List<NameValuePair> addTotal(String total) throws ClientException {
        String[] tokens = total.split(",");
        if (tokens.length != 2) {
            throw new ClientException("Wrong format of total amount: " + total);
        }

        return Arrays.asList(
                new BasicNameValuePair(TOTAL_ZL, tokens[0]),
                new BasicNameValuePair(TOTAL_GR, tokens[1]));
    }

    private List<NameValuePair> addDate(LocalDate date) {
        DecimalFormat df = new DecimalFormat("00");
        return Arrays.asList(
                new BasicNameValuePair(YEAR, String.valueOf(date.getYear())),
                new BasicNameValuePair(MONTH, df.format(date.getMonthValue())),
                new BasicNameValuePair(DAY, df.format(date.getDayOfMonth())));
    }


    private Header[] createHeaders(String token) {
        return new Header[]{
                new BasicHeader("x-csrf-token", token),
                new BasicHeader("cookie", cookies),
                new BasicHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8"),
                new BasicHeader("origin", ADDRESS),
                new BasicHeader("referer", ADDRESS),
                new BasicHeader("user-agent", USER_AGENT)};
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
