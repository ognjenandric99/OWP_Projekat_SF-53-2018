var request = new XMLHttpRequest(),
        url = 'FilmoviServlet',
        data = {
		"action":"ucitajFilm",
		"filmID":"1"
		};
    // when the server is done and it came back with the data you can handle it here
    request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // do whatever you want!
            console.log("The request and response was successful!");
            console.log(request.responseText);
       }
    };

    // method post, your giving it the URL, true means asynchronous
    request.open('POST', url, true);
    request.send(data);