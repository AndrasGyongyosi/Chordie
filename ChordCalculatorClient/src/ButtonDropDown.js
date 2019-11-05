import React, { Component } from 'react';
import CatchList from './CatchList';

class ButtonDropDown extends Component {
    constructor() {
        super();
        
        this.state = {
          showMenu: false,
          lists: ["MyList1", "MyList2"]
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
                      {this.state.lists.map(list =>
                      <CatchList listName={list}></CatchList>
                      )}
                    <button className="minibutton"> NewList </button>
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