import React from 'react';
import './App.css';
import OAuth from "./OAuth";
import ChordChooserPage from "./ChordChooserPage";



class App extends React.Component {
    state={ token: null,
            logged_user: "nobody",
            backGroundPrimaryColor: "#ffffff",
            backGroundSecondaryColor: "#f8f9fa",
            foreGroundColor: "#000000",
            buttonColor: "#33cc33"
    };

    componentDidMount(){
        var expire_date = localStorage.getItem("token_expired");
        var access_token = localStorage.getItem("google_access_token");
        var jwtToken = localStorage.getItem("jwtToken");
        var user_email = localStorage.getItem("user_email");
        if (new Date(expire_date)>new Date()){
            this.setState({google_access_token: access_token,
                                logged_user: (user_email==null)? "nobody" : user_email,
                                token: jwtToken,
            });
            var now = new Date();
            localStorage.setItem("token_expired",new Date(now.setMinutes(now.getMinutes()+5)));
            //axios.defaults.headers.post["Authorization"]=jwtHeader;
            //axios.defaults.headers.post["client_id"]="658977310896-knrl3gka66fldh83dao2rhgbblmd4un9.apps.googleusercontent.com";
            //axios.defaults.headers.get["Authorization"]=jwtHeader;
            //axios.defaults.headers.get["client_id"]="658977310896-knrl3gka66fldh83dao2rhgbblmd4un9.apps.googleusercontent.com";
        }

    }
    setToken(id_token, email){
        var now = new Date();
        var jwtToken = id_token;//"Bearer "+id_token;
        localStorage.setItem("jwtToken",jwtToken);
        localStorage.setItem("token_expired", new Date(now.setMinutes(now.getMinutes()+5)));
        localStorage.setItem("user_email", email);
        this.setState({logged_user: email,
                            token: jwtToken});
        //this.state.logged_user = email;
        //this.state.token = jwtToken;
    }
    changeBackgroundPrimaryColor(event){
        this.setState({backGroundPrimaryColor: event.target.value});

        var style = document.createElement('style');
        document.head.appendChild(style);
        style.sheet.insertRule(':root {--backgroundPrimary: '+event.target.value+'}');

        console.log("backGroundPrimaryColorChange");
        return;
    }

    changeBackgroundSecondaryColor(event){
        this.setState({backGroundSecondaryColor: event.target.value});

        var style = document.createElement('style');
        document.head.appendChild(style);
        style.sheet.insertRule(':root {--backgroundSecondary: '+event.target.value+'}');

        console.log("backGroundSecondaryColorChange");
        return;
    }

    changeButtonColor(event){
        this.setState({buttonColor: event.target.value});

        var style = document.createElement('style');
        document.head.appendChild(style);
        style.sheet.insertRule(':root {--buttons: '+event.target.value+'}');

        let bcButton = document.getElementById("buttonpicker");
        console.log(bcButton.value);
        console.log("buttonColorChange");
    }
    changeForegroundColor(event){
        this.setState({foreGroundColor: event.target.value});

        var style = document.createElement('style');
        document.head.appendChild(style);
        style.sheet.insertRule(':root {--foreground: '+event.target.value+'}');

        let fgcButton = document.getElementById("foregroundpicker");
        console.log(fgcButton.value);
        console.log("foreGroundColorChange");
    }
    render() {
        return (
          <div className="row App">
                <div className="col-lg-12">
                <div className="row">
                        <div className="col-lg-3">
                            <label for="backgroundprimarypicker">Background1: </label>
                            <input type="color" id="backgroundprimarypicker" value={this.state.backGroundPrimaryColor} onChange={this.changeBackgroundPrimaryColor.bind(this)}></input>    
                            <p>{this.state.backGroundPrimaryColor}</p>
                        </div>
                        <div className="col-lg-3">
                            <label for="backgroundsecondarypicker">Background2: </label>
                            <input type="color" id="backgroundsecondarypicker" value={this.state.backGroundSecondaryColor} onChange={this.changeBackgroundSecondaryColor.bind(this)}></input>    
                            <p>{this.state.backGroundSecondaryColor}</p>
                        </div>
                        <div className="col-lg-3">
                            <label for="buttonpicker">Buttons: </label>
                            <input type="color" id="buttonpicker" value={this.state.buttonColor} onChange={this.changeButtonColor.bind(this)} ></input>    
                            <p>{this.state.buttonColor}</p>
                        </div>
                        <div className="col-lg-3">
                            <label for="foregroundpicker">Foreground: </label>
                            <input type="color" id="foregroundpicker" value={this.state.foreGroundColor} onChange={this.changeForegroundColor.bind(this)} ></input>    
                            <p>{this.state.foreGroundColor}</p>
                        </div>
                    </div>
                    <hr></hr>
                    <div className="row">
                        <div className="col-lg-2">
                        <span>Logged in by {this.state.logged_user}</span><br/>
                        {this.state.img==null? <p></p>:<div><p>{this.state.img}</p><img src={this.state.img}></img></div>}
                        </div>
                        <div className="col-lg-8">
                            <h1 className="align-middle">Chordie</h1>
                        </div>
                        <div className="col-lg-2">
                            <OAuth className="align-right" ancestor={this}></OAuth>
                        </div>
                    </div>
                    
                  <ChordChooserPage token={this.state.token}></ChordChooserPage>
                </div>
            </div>    
        );
  }
}

export default App;