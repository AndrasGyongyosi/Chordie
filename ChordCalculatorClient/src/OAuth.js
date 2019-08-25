import React, { Component } from 'react'
import {GoogleLogin, GoogleLogout} from 'react-google-login'
import axios from "axios";

const url= 'http://localhost:8080/'

export default class OAuth extends Component {
    state={}
    render() {
        const responseGoogle = (response) => {
            console.log("LoginSuccess");

            let newUserURL = url + "newUser/";
            console.log(response);
            let passedParameters = {
                "email": response.profileObj.email,
                "token": response.tokenId,
            };
            //this.setState({"img":response.profileObj.imageURL})
            this.state.img=response.profileObj.imageURL;
            console.log(passedParameters);
            axios.post(newUserURL, passedParameters)
                .then(res=>{
                    if (res){
                        console.log("New User added.");
                        this.props.ancestor.setToken(response.Zi.access_token, response.profileObj.email);
                    }
                    else {
                        console.log("User adding failed.");
                    }
                })

            console.log(response);
        }
        const responseGoogleFailure = (response) => {
            console.log("LoginFail");
            this.props.ancestor.setToken(response.tokenId, response.profileObj.email);
            console.log(response);
        }
        const logout = () => {
            console.log("Logout");
            this.props.ancestor.setToken(null, "nobody");
        }
        return (
            <div>
                <span>Logged in by {this.props.ancestor.state.logged_user}</span><br/>
                {this.state.img==null? <p></p>:<div><p>{this.state.img}</p><img src={this.state.img}></img></div>}

                {(this.props.ancestor.state.logged_user=="nobody") ?
                    (<GoogleLogin
                    clientId="902904919760-8uea185c8i6meeap7gga8lhjivgtt33t.apps.googleusercontent.com"
                    buttonText="Login"
                    onSuccess={responseGoogle}
                    onFailure={responseGoogleFailure}
                    cookiePolicy={'single_host_origin'}
                    />)
            :
                    (   <GoogleLogout
                        buttonText="Logout"
                        onLogoutSuccess={logout}
                    >
                    </GoogleLogout>)
                }
            </div>
            )
    };
}