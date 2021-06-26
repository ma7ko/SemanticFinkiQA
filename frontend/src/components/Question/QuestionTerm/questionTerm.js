import React, {Component} from 'react';
import QuestionService from '../../../services/questionService';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faThumbsDown, faThumbsUp} from "@fortawesome/free-solid-svg-icons";
import {Link, useHistory} from 'react-router-dom';
import './questions.css';

class QuestionTerm extends Component {
    constructor(props) {
        super(props);
        this.saveLikedQuestion = this.saveLikedQuestion.bind(this);
        this.saveDislikedQuestion = this.saveDislikedQuestion.bind(this);
        this.state = {

        }

    }

    saveLikedQuestion() {
        this.props.term.likedBy.push(this.props.currentUser);
        this.props.likeQuestion(this.props.term, this.props.currentUser);
        console.log(this.props.term);
    }

    saveDislikedQuestion() {
        this.props.term.likedBy.push(this.props.currentUser);
        this.props.dislikeQuestion(this.props.term, this.props.currentUser);
        console.log(this.props.term);
    }

    render() {
        return (
            <div className="card questionTerm">
                <div className="card-header text-center header-card">
                    <div className={"row"}>
                        <div className={"col-md-9"}>
                            <div>
                            <ul className="nav nav-pills card-header-pills tags">
                                {console.log(this.props.term)}
                                {
                                    this.props.term.tags?.map((tag) => {
                                        return (
                                            <li key={tag}
                                                className="nav-item">
                                                <h3><span className={"badge badge-info mr-2 ml-2 text-light"}>{tag}</span></h3>
                                            </li>
                                        )
                                    })
                                }
                            </ul>
                            </div>
                        </div>
                        <div className={"col-md-3"}> { ((this.props.currentUser?.username === this.props.term.writer?.username) || (this.props.currentUser?.roles?.includes("ROLE_ADMIN"))) &&
                        <div id={`${this.props.term.writer}funct`}>
                            <a title={"Delete"} className={"btn btn-danger"}
                               onClick={() => {this.props.deleteQuestion(this.props.term["@id"])}}>Delete</a>
                            <Link className={"btn btn-info ml-2"}
                                  onClick={() => {this.props.resetCurrentQuestion(); this.props.getQuestion(this.props.term["@id"])}}
                                  to={`/questions/form/${this.props.term.uri}`}>
                                Edit
                            </Link></div> }

                        </div>
                    </div>
                </div>
                <div className="card-body">
                    <div className={"row"}>
                        <div className={"col-md-9"}> {console.log(this.props.term.description)}
                            <h5 className="card-title">{<Link onClick={() => {this.props.getQuestion(this.props.term["@id"]); window.scrollTo(0, 0);}} to={`/questions/details/${this.props.term.uri}`}> {this.props.term.title} </Link>} - <Link onClick={() => {this.props.setProfileUser(this.props.term.writer)}} to={`/profile/${this.props.term.writer.uri}`}> {this.props.term.writer.username} </Link></h5>
                            <p className="card-text">{this.props.term.description}</p>
                        </div>
                        <div className={"col-md-3"}>
                            <div className={"card"}>
                                <div className={"card-header"}>
                                    <p className={"reviews-header text-center"}>Reviews</p>
                                </div>
                                <div className={"card-body"}>
                                    <div className={"small-icon"}>
                                        <div className={"items-icon"}>
                                            <div className={'innermost'}><FontAwesomeIcon className={this.props.term?.likedBy?.map(term1 => term1.username).includes(this.props.currentUser?.username) ? "change-color custom-size" : 'custom-size'} onMouseOut={(e) => {!this.props.term?.likedBy?.map(term1 => term1.username).includes(this.props.currentUser?.username) && e.target.classList.remove("change-color")}} onMouseOver={(e) => { console.log(e.target); e.target.classList.add("change-color")}} onClick={() => {this.saveLikedQuestion()}}  icon={faThumbsUp}/></div>
                                            <div className={'innermost'}>{this.props.term?.likedBy?.length}</div>
                                        </div>
                                        <div className={"items-icon"}>
                                            <div className={'innermost'}><FontAwesomeIcon className={this.props.term?.dislikedBy?.map(term1 => term1.username).includes(this.props.currentUser?.username) ? "change-color custom-size" : 'custom-size'} onMouseOut={(e) => {!this.props.term?.dislikedBy?.map(term1 => term1.username).includes(this.props.currentUser?.username) && e.target.classList.remove("change-color")}} onMouseOver={(e) => { console.log(e.target); e.target.classList.add("change-color")}} onClick={() => {this.saveDislikedQuestion()}}  icon={faThumbsDown}/></div>
                                            <div className={'innermost'}>{this.props.term?.dislikedBy?.length}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <h6 className={"text-secondary "}><i> Posted on {this.props.term.date.split("T")[0]} </i></h6>
                    </div>
                    {/*<div> { this.props.currentUser?.username &&*/}
                    {/*<Link onClick={() => {this.props.getQuestion(this.props.term["@id"]); window.scrollTo(0, 0);}}*/}
                    {/*      to={`/questions/details/${this.props.term.uri}`}*/}
                    {/*      className="btn btn-primary">See more</Link> }*/}
                    {/*</div>*/}
                    {/*<div> { !this.props.currentUser?.username &&*/}
                    {/*<Link to={`/questions/details/${this.props.term.uri}`}*/}
                    {/*      onClick={() => {this.props.getQuestion(this.props.term["@id"])}}*/}
                    {/*      className="btn btn-primary">See more</Link> }*/}
                    {/*</div>*/}
                </div>
            </div>
        );
    }

    componentDidMount() {
    }

}

export default QuestionTerm;