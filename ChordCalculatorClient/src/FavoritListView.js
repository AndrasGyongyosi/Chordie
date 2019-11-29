import React, { Component } from 'react'
import ModalDialogWithoutFooter from './Modal/ModalDialogWithoutFooter';
import FavoritCatchView from './FavoritCatchView.js';
import CatchView from './CatchView';

export default class FavoritListView extends Component {
    title = "Lists"
    state = {show: false};

    componentDidMount(){
        this.setState({show : this.props.show});
    }
    showModal = () => {
        console.log(this.props);
        this.setState({show: true});
    };

    hideModal = () => {
        this.setState({show: false});
    };

    handleReject = () => {
        this.hideModal();
    };

    handleAccept = () => {
        this.props.onAccept();
        this.hideModal();
    };
    render() {
        return (
            <div>
                <ModalDialogWithoutFooter title={this.title} show={this.state.show} handleReject={this.handleReject}>
                                {this.props.favoritLists != null ? 
                                (this.props.favoritLists.map(list=>
                                    <div class="row">
                                        <div class="col-lg-10">
                                            <h3>{"List name: "+list.name}</h3>
                                        </div>
                                        <div class="col-lg-2">
                                        {list.catches.map(catcha =>
                                            //<FavoritCatchView catcha={catcha}></FavoritCatchView>
                                            <CatchView catcha={catcha} view="list" bundDif={5}></CatchView>
                                            )
                                        }
                                        <br/>
                                        </div>
                                    </div>
                                )) : <h2>There is no list for this user.</h2>
                                }
                </ModalDialogWithoutFooter>
                <button className="btn btn-outline-secondary" onClick={this.showModal}>{this.title}</button>
            </div>
            )
    };
}