import React from "react";

function ModalDialog(props) {

    const showHideClassName = props.show ? "modal" : "d-none";

    return (props.show ? (
            <div className={showHideClassName} tbIndex="-1">
                <div className="modal-dialog modal-lg">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h3 className="modal-title">{props.title}</h3>
                            <button type="button" className="close" onClick={props.handleReject}>
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <p>
                                {props.children}
                            </p>
                        </div>
                        <div className="modal-footer">
                            <form>
                                <button type="button" className="btn btn-danger" onClick={props.handleReject}>Reject
                                </button>
                                <button type="button" className="btn btn-success" onClick={props.handleAccept}>Accept
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        ) : ""

    )
}

export default ModalDialog;