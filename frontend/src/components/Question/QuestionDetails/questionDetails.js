import React, {Component, useState} from 'react';
// import AnswerTerm from '../../Answers/AnswerTerm/answerTerm';
import AnswerForm from '../../Answer/AnswerForm/answerForm'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFeatherAlt, faThumbsDown, faThumbsUp} from "@fortawesome/free-solid-svg-icons";
import '../QuestionTerm/questions.css';
// import Pagination from "../../Pagination/pagination";
import QuestionService from "../../../services/questionService";
import AnswerService from "../../../services/answerService";
import AnswerTerm from "../../Answer/AnswerTerm/answerTerm";
import {Link} from "react-router-dom";
import Pagination from "../../Pagination/pagination";
import './questionDetails.css';

class QuestionDetails extends Component {

    constructor(props) {
        super(props);
        this.getQuestion = this.getQuestion.bind(this);
        this.saveLikedQuestion = this.saveLikedQuestion.bind(this);
        this.saveDislikedQuestion = this.saveDislikedQuestion.bind(this);
        this.likeAnswer = this.likeAnswer.bind(this);
        this.dislikeAnswer = this.dislikeAnswer.bind(this);
        this.setCurrentPage = this.setCurrentPage.bind(this);

        this.state = {
            answers:[],
            currentQuestion: this.props.question,
            currentPage: 1,
            lastPost: 2,
            firstPost: 0,
            postsPerPage: 2,
        }
    }

    saveLikedQuestion(question, user) {
        QuestionService.likeQuestion(question, user).then((response) => {
            this.props.getQuestion(question["@id"])
        })

    }

    saveDislikedQuestion(question, user) {
        QuestionService.dislikeQuestion(question, user).then((response) => {
            this.props.getQuestion(question["@id"])
        })
    }

    likeAnswer(answer) {
        AnswerService.likeAnswer(answer, this.props.currentUser).then((response) => {
            this.props.getQuestion(this.props.question["@id"])
        })
    }

    dislikeAnswer(answer) {
        AnswerService.dislikeAnswer(answer, this.props.currentUser).then((response) => {
            this.props.getQuestion(this.props.question["@id"])
        })
    }

    getQuestion(questionId) {
        QuestionService.getQuestion(questionId)
            .then((response) => {
                this.setState({
                    currentQuestion: response.data
                })
            })
    }


    setCurrentPage(page) {
        this.setState({
            currentPage: page,
            lastPost: page * this.state.postsPerPage,
            firstPost: page * this.state.postsPerPage - this.state.postsPerPage,
        });
        console.log(page * this.state.postsPerPage);
        console.log(this.state.indexOfLastPost);
    }


    render() {

        return (
            <div className={"container"}>
                {console.log("This is sent")}
                {console.log(this.props.question)}
                <div className={"row"}>
                    <div className={"col-md-6"}>
                        <div className={"card mt-4"}>
                            <h5 className={"card-header"}>{this.props.question?.title} - <Link onClick={() => {this.props.setProfileUser(this.props.question?.writer)}} to={`/profile/${this.props.question?.writer?.uri}`}>{this.props.question?.writer?.username}</Link></h5>
                            <div className={"card-body"}>
                                <div className={"row"}>
                                    <div className={"col-md-8"}>
                                        {this.props.question?.description}
                                    </div>


                                    <div className={"col-md-4"}>
                                        <div className={"small-icon"}>
                                            <div className={"items-icon"}>
                                                <div className={'innermost'}><FontAwesomeIcon
                                                    className={this.props.question?.likedBy?.map(term => term.username).includes(this.props.currentUser?.username) ? "change-color custom-size" : 'custom-size'}
                                                    onMouseOut={(e) => {
                                                        !this.props.question?.likedBy.map(term => term.username).includes(this.props.currentUser?.username) && e.target.classList.remove("change-color")
                                                    }} onMouseOver={(e) => {
                                                    console.log(e.target);
                                                    e.target.classList.add("change-color")
                                                }} onClick={(e) => {
                                                    this.saveLikedQuestion(this.props.question, this.props.currentUser)
                                                }} icon={faThumbsUp}/></div>
                                                <div className={'innermost'}>{this.props.question?.likedBy?.length}</div>
                                            </div>
                                            <div className={"items-icon"}>
                                                <div className={'innermost'}><FontAwesomeIcon
                                                    className={this.props.question?.dislikedBy?.map(term => term.username).includes(this.props.currentUser?.username) ? "change-color custom-size" : 'custom-size'}
                                                    onMouseOut={(e) => {
                                                        !this.state.currentQuestion?.dislikedBy?.map(term => term.username).includes(this.props.currentUser?.username) && e.target.classList.remove("change-color")
                                                    }} onMouseOver={(e) => {
                                                    console.log(e.target);
                                                    e.target.classList.add("change-color")
                                                }} onClick={(e) => {
                                                    this.saveDislikedQuestion(this.props.question, this.props.currentUser)
                                                }} icon={faThumbsDown}/></div>
                                                <div className={'innermost'}>{this.props.question?.dislikedBy?.length}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className={'row'}>
                            <div className={'col-md-12 mt-4'}>
                                <div className={"container mt-4"}>
                                    <AnswerForm questionId={this.props.question["@id"]}
                                                numberOfRows={"7"}
                                                onAddAnswer={this.props.onAddAnswer}
                                                currentUser={this.props.currentUser}
                                                props={this.props.props}
                                                getQuestion={this.props.getQuestion}/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={"col-md-6"}>
                        <div className={'mt-4'}>
                            <div className={'small-icon'}>
                                <h4>Answers</h4>
                                <div><Pagination mainPage={false} totalPosts={this.props.question?.answer?.length} postsPerPage={this.state.postsPerPage} currentPage={this.state.currentPage} paginate={this.setCurrentPage}/></div>
                            </div>
                        </div>
                        <div className={"container box-fix height-change"}>
                            {console.log(this.props.answers)}

                            {this.props.question.answer?.slice(this.state.firstPost, this.state.lastPost).map((term) => {
                                return <AnswerTerm key={`${term.title}${term.question?.title}`}
                                                   term={term}
                                                   questionId={this.props.props.match.params.id}
                                                   onAnswerDelete={this.props.onAnswerDelete}
                                                   onAnswerEdit={this.props.onAnswerEdit}
                                                   onEditAnswer={this.props.onEditAnswer}
                                                   onAddAnswer={this.props.onAddAnswer}
                                                   likeAnswer={this.likeAnswer}
                                                   dislikeAnswer={this.dislikeAnswer}
                                                   currentUser={this.props.currentUser}
                                                   props={this.props.props}
                                                   getQuestion={this.props.getQuestion}
                                                   setProfileUser={this.props.setProfileUser}/>
                            })}

                            {this.props.question.answer?.length === 0 && <div className={"not-found fit-c"}><div className={'not-found text-center'}><FontAwesomeIcon size={'4x'} icon={faFeatherAlt}/> <p className={'text-center'}>No answers yet</p></div></div>}
                        </div>
                        {console.log(this.state.answers)}
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {

    }


}

export default QuestionDetails;