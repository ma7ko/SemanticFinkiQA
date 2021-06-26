import './answerTerm.css';
import React, {useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFeatherAlt, faThumbsDown, faThumbsUp, faTrash, faTrashAlt} from "@fortawesome/free-solid-svg-icons";
import {Link} from "react-router-dom";
import AnswerForm from "../AnswerForm/answerForm";
import AnswerService from "../../../services/answerService";

const AnswerTerm = (props) => {


    return (
        <div className={"card mt-4"}>
            <div className={"card-header header-card"}>
                <div className={"fl-cont"}>
                    <div className={"fl-item"}>
                        <div className={'mt-1'}><Link onClick={() => {props.setProfileUser(props.term?.writer)}} to={`/profile/${props.term?.writer?.uri}`}>{props.term.writer?.username}</Link></div>
                    </div>
                    <div className={"fl-item"}> { ((props.currentUser?.username === props.term.writer?.username) || (props.currentUser?.roles?.includes("ROLE_ADMIN"))) &&
                    <div className={'header-buttons'}>
                        <a title={"Delete"} className={"btn btn-danger"}
                           onClick={() => {AnswerService.deleteAnswer(props.term["@id"]).then((response)=>{props.getQuestion(props.term.question)})}}><FontAwesomeIcon size={'1x'} icon={faTrashAlt}/>
                        </a>
                    </div>}
                    </div>
                </div>
            </div>
            <div className={"card-body"}>
                <div className={'border-bottom mb-3'}>
                    <p id={`static${props.term.id}${props.questionId}`}>{props.term.explanation}</p> {console.log(props.term)}
                    <div id={`form${props.term.id}${props.questionId}`} className={"hide"} >

                        <AnswerForm questionId={props.questionId}
                                    numberOfRows={"4"}
                                    answer={props.term}
                                    onEditAnswer={props.onEditAnswer}
                                    currentUser={props.currentUser}
                                    props={props.props}
                                    toogleButton={() => {document.getElementById(`form${props.term.id}${props.questionId}`).classList.toggle("hide"); document.getElementById(`static${props.term.id}${props.questionId}`).classList.toggle("hide");}}/>

                    </div>
                </div>
                <div className={'main-fl'}>
                    <div className={'fl-ch'}>
                        {props.term.date?.split("T")[0]}
                    </div>
                    <div className={'fl-ch'}>
                        <div className={"small-icon overr2"}>
                            <div className={"items-icon overr"}>
                                <div className={'innermost'}><FontAwesomeIcon className={props.term?.likedBy?.map(term1 => term1.username).includes(props.currentUser?.username) ? "change-color custom-size ic" : 'custom-size ic'} onMouseOut={(e) => {!props.term?.likedBy?.map(term1 => term1.username).includes(props.currentUser?.username) && e.target.classList.remove("change-color")}} onMouseOver={(e) => { console.log(e.target); e.target.classList.add("change-color")}} onClick={() => {props.likeAnswer(props.term)}} icon={faThumbsUp}/></div>
                                <div className={'innermost'}>{props.term.likedBy.length}</div>
                            </div>
                            <div className={"items-icon overr"}>
                                <div className={'innermost'}><FontAwesomeIcon className={props.term?.dislikedBy?.map(term1 => term1.username).includes(props.currentUser?.username) ? "change-color custom-size ic" : 'custom-size ic'} onMouseOut={(e) => {!props.term?.dislikedBy?.map(term1 => term1.username).includes(props.currentUser?.username) &&e.target.classList.remove("change-color")}} onMouseOver={(e) => { console.log(e.target); e.target.classList.add("change-color")}} onClick={() => {props.dislikeAnswer(props.term)}} icon={faThumbsDown}/></div>
                                <div className={'innermost'}>{props.term.dislikedBy.length}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AnswerTerm;