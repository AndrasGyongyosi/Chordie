import React from 'react';
import './App.css';
import OAuth from "./OAuth";
import ChordChooserPage from "./ChordChooserPage";

const url= 'http://localhost:8080/'


class App extends React.Component {
    state={ token: null,
            logged_user: "nobody",
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
    render() {
        return (
          <div className="row App">
              <div className="col-lg-12">
                  <div className="row">
                      <div className="col-lg-10">
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