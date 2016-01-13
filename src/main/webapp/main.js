function handleBrowseClick() {
    var fileinput = document.getElementById("browse");
    fileinput.click();
}

function handlechange() {
	var fileinput = document.getElementById("browse");
	var textinput = document.getElementById("filename");
	console.log(fileinput.value);
	var files = fileinput.files;
	var list = "";
	for(var i = 0; i < files.length; ++i){
		list += files[i].name +"; ";
	}
	textinput.value = list;
}