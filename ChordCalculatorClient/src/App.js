import React from 'react';
import './App.css';
import OAuth from "./OAuth";
import ChordChooserPage from "./ChordChooserPage";
import myURLs from "./myURLs.js";
import axios from 'axios';
import ColorPicker from "./ColorPicker";



class App extends React.Component {
    state={ token: null,
            logged_user: undefined,
            favoritLists: [],
    };
    getFavoritLists = (token) => {
        if (token!=null){
            let listURL = myURLs.getURL() + "list/"+token;
            console.log(listURL);
                    axios.get(encodeURI(listURL))
                        .then(res => {
                            this.setState({favoritLists :res.data});
                            //window.location.reload();
                        }).catch(error => this.setState({error, catchLoaded: false}));
            }
        }

    componentDidMount(){
        var expire_date = localStorage.getItem("token_expired");
        var access_token = localStorage.getItem("google_access_token");
        var jwtToken = localStorage.getItem("jwtToken");
        var user_email = localStorage.getItem("user_email");
        if (new Date(expire_date)>new Date()){
            this.setState({google_access_token: access_token,
                                logged_user: user_email,
                                token: jwtToken,
            });
            var now = new Date();
            localStorage.setItem("token_expired",new Date(now.setMinutes(now.getMinutes()+5)));
        }
        this.getFavoritLists(jwtToken);
    }
    setToken(id_token, email){
        var now = new Date();
        var jwtToken = id_token;
        if (id_token==null){
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("token_expired");
            localStorage.removeItem("user_email");
        }
        else{
            localStorage.setItem("jwtToken",jwtToken);
            localStorage.setItem("token_expired", new Date(now.setMinutes(now.getMinutes()+5)));
            localStorage.setItem("user_email", email);
            this.setState({logged_user: email,
                                token: jwtToken});
        }
        this.getFavoritLists(id_token);
    }

    render() {
        return (
          <div className="row App">
                <div className="col-lg-12">
                    
                    <hr></hr>
                    <div className="row">
                        <div className="col-lg-2">
                        <span>Logged in by {this.state.logged_user==undefined ? "nobody" : this.state.logged_user}</span><br/>
                        {this.state.img==null? <p></p>:<div><p>{this.state.img}</p><img src={this.state.img}></img></div>}
                        </div>
                        <div className="col-lg-8">
                            <h1 className="align-middle">Chordie</h1>
                        </div>
                        <div className="col-lg-2">
                            <OAuth className="align-right" ancestor={this}></OAuth>
                        </div>
                    </div>
                    
                  <ChordChooserPage token={this.state.token} favoritLists={this.state.favoritLists}></ChordChooserPage>
                </div>
            </div>    
        );
  }
}

export default App;