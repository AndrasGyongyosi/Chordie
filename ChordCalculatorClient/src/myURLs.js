class myURLs {

    static getURL(){
        let clientUrl = window.location.href;
        let clientUrlSplit = clientUrl.split(":");
        let serverUrl = clientUrlSplit[0] + ":" + clientUrlSplit[1] + ":8080/";

        let frontUrl="http://chordie.ddns.net";
        //serverUrl = frontUrl+":8080/";
        return serverUrl;
    }
}export default myURLs;