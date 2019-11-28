import React from 'react';
import './App.css';

class ColorPicker extends React.Component {
    state={ 
        backGroundPrimaryColor: "#ffffff",
        backGroundSecondaryColor: "#f8f9fa",
        foreGroundColor: "#000000",
        buttonColor: "#33cc33"
    };

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

    render(){
        return  (<div className="row">
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
    </div>);
    }
}
export default ColorPicker;