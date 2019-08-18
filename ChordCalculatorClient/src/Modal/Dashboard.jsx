import React, {Component} from "react";

import ModalDialog from "./ModalDialog";

class Dashboard extends Component {

    state = {show: false};

    showModal = () => {
        this.setState({show: true});
    };

    hideModal = () => {
        this.setState({show: false});
    };

    handleReject = () => {
        this.props.onReject();
        this.hideModal();
    };

    handleAccept = () => {
        console.log(this.props);
        this.props.onAccept();
        this.hideModal();
    };

    render() {
        return (
            <div>
                <ModalDialog title={this.props.title} show={this.state.show} handleReject={this.handleReject}
                             handleAccept={this.handleAccept}>
                    {this.props.children}
                </ModalDialog>
                <a href="#" onClick={this.showModal}>{this.props.title}</a>
            </div>
        )
    }
}

export default Dashboard;