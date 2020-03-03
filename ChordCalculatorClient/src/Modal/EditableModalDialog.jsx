import React, {Component}  from "react";

class EditableModalDialog extends Component{

    render(){
        return (this.props.show ? (
                <div className={this.props.show ? "modal" : "d-none"} tbIndex="-1">
                    <div className="modal-dialog modal-lg">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h3 className="modal-title">{this.props.title}</h3>
                                <button type="button" class="btn btn-warning btn-sm minibutton close" onClick={()=>this.props.handleEdit()} hidden={this.props.isPublic || this.props.editMode}><i class="fa fa-edit"></i></button>
                                <button type="button" class="btn btn-danger btn-sm minibutton close " onClick={()=>this.props.handleDelete()} hidden={!this.props.editMode}><i class="fa fa-close"></i></button>
                                <button type="button" className="close" onClick={this.props.handleReject}>
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <p>
                                    {this.props.children}
                                </p>
                            </div>
                            <div className="modal-footer" hidden={!this.props.editMode}>
                                <form>
                                    <button type="button" className="btn btn-danger" onClick={()=>this.props.handleReject()}>Reject
                                    </button>
                                    <button type="button" className="btn btn-success" onClick={()=>this.props.approveEdit()}>Accept
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            ) : ""

        )
    }
}

export default EditableModalDialog;