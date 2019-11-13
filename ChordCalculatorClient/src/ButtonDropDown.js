import React, { Component } from 'react';
import CatchList from './CatchList';
import myURLs from './myURLs.js';
import axios from "axios";


class ButtonDropDown extends Component {
    constructor() {
        super();
        
        this.state = {
          showMenu: false,
          favoritLists : []
        };
        
        this.showMenu = this.showMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
      }
      
      componentDidMount(){
        this.getLists();
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
      getLists(){
        if (this.props.token!=null){
        let listURL = myURLs.getURL() + "favorit/lists/"+this.props.token;
                axios.get(encodeURI(listURL))
                    .then(res => {
                        console.log(res.data);
                        this.setState(
                            {
                                favoritLists: res.data
                            });

                    }).catch(error => this.setState({error, catchLoaded: false}));
            }
          }
      newList(){
        let newListURL = myURLs.getURL() + "favorit/newlist"; 
        let passedParameters = {
          "name": "example",
          "userToken": this.props.token,
      };
      console.log(passedParameters);
      axios.post(newListURL, passedParameters)
          .then(res=>{
              console.log(res);
              window.location.reload();
      });
      }

      render() {
        return (
          <div>
            <button onClick={this.showMenu}>
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
                      {this.state.favoritLists.map(list => 
                      <CatchList listName={list.name}></CatchList>
                      )}
                    <button className="minibutton" onClick={()=>this.newList()}> New List... </button>
                  </div>
                )
                : (
                  null
                )
            }
          </div>
        );
      }
    }

export default ButtonDropDown;