import React, { Component } from 'react';
import CatchList from './CatchList';
import myURLs from './myURLs.js';
import axios from "axios";
import ModalDialog from "./Modal/ModalDialog"

class ButtonDropDown extends Component {
    constructor() {
        super();
        
        this.state = {
          showMenu: false,
          newList: false,
        };
        
        this.showMenu = this.showMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
      }

      showMenu(event) {
        event.preventDefault();
        
        this.setState({ showMenu: true }, () => {
          document.addEventListener('click', this.closeMenu);
        });
      }
      
      closeMenu(event) {
        
        if (!this.dropdownMenu.contains(event.target)) {
          
          this.setState({ showMenu: false }, () => {
            document.removeEventListener('click', this.closeMenu);
          });  
          
        }
      }
      addToList(list){
        let catcha = this.props.catch.props.catcha;
        let strings = this.props.catch.props.strings;
        let parameters = {
          "catch" : catcha.fingerPoints,
          "listToken" : list.token,
          "instrument": this.props.instrument.name,
          "chord": "need_to_do",
        };
        console.log(parameters);
        let addToListURL = myURLs.getURL() + "favorit/addToList"; 
        
      
        axios.post(addToListURL, parameters)
            .then(res=>{
                console.log(res);
                //window.location.reload();
        });

      }
      newList(){
        this.setState({
          newList: true,
        });
      }
      render() {
        return (
          <div>
            <button className="btn btn-outline-info minibutton" onClick={this.showMenu}>
            <i class="fa fa-plus"></i>
            </button>
            
            {
              this.state.showMenu
                ? (
                  <div
                    className="menu"
                    ref={(element) => {
                      this.dropdownMenu = element;
                    }}
                  >
                      {this.props.favoritLists.map(list => 
                      <CatchList listName={list.name} addToList={()=>this.addToList(list)}></CatchList>
                      )}
                    <button className="btn btn-outline-secondary minibutton" onClick={()=>this.newList()}> New List... </button>
                  <ModalDialog title="Edit" show={this.state.newList==true} handleAccept={this.handleAccept} handleReject={this.handleReject} >
                    <div className="container">
                      <div className="row">
                        <div className="col-lg-6">
                            <label htmlFor="listName">List name</label>
                            <input type="text" className="form-control" id="listName" placeholder="List name" autofocus="true" defaultValue=""/>
                        </div>
                      </div>
                    </div>
                  </ModalDialog>
              </div>              
                )
                : (
                  null
                )
            }
          </div>
        );
      }

      handleAccept = () => {

        let newListURL = myURLs.getURL() + "favorit/newlist"; 
        let passedParameters = {
          "name": document.getElementById("listName").value,
          "userToken": this.props.token,
      };
      console.log(passedParameters);
      axios.post(newListURL, passedParameters)
          .then(res=>{
              console.log(res);
              window.location.reload();
      });
        this.setState({newInstrument : false});
      }
      handleReject = () => {
        this.setState({newInstrument : false});
      }
    }

export default ButtonDropDown;