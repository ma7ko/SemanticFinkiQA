import './answerForm.css';
import React, {Component, useState} from 'react';
import {Link, useHistory} from "react-router-dom";
import AnswerService from "../../../services/answerService";

class AnswerForm extends Component {

    constructor(props) {
        super(props);

        this.onChangeField = this.onChangeField.bind(this);
        this.state = {
            explanation: "",
            likedBy: [],
            dislikedBy: [],
            questionId: this.props.questionId,
            username: "",
            answers: []
        }
    }

    onChangeField(e) {
        this.setState({
            [e.target.name]: e.target.value
        });
    }

    saveAnswer = (explanation, likes, dislikes, questionId, username) => {
        console.log(this.props)
        AnswerService.addAnswer(explanation, likes, dislikes, questionId, this.props.currentUser.id)
            .then((response) => {
                if (response) {
                    this.props.getQuestion(questionId);
                    console.log("Response ")
                    console.log(response)
                }
            });

        this.setState({explanation: ""});
    }

    editAnswer = (id, explanation, likes, dislikes, questionId, username) => {
        console.log(id);
        console.log(explanation)
        console.log(likes)
        console.log(dislikes)
        console.log(this.props.questionId);
        console.log(questionId)
        console.log(username)
        this.props.onEditAnswer(id, explanation, likes, dislikes, questionId, username);
        this.props.toogleButton();
    }

    getAnswersFromQuestionId = (id) => {
        // this.getQuestion(id);
        // FinkiQAService.getAnswersByQuestionId(id)
        //     .then((data) => {
        //         console.log(data);
        //         this.setState({
        //             answers: data.data
        //         });
        //
        //     });
    }

    render() {
        return (
            <div className={"container"}>
                <div className="row">
                    <div className="col-md-5 pb-4">
                        <form onSubmit={(e) => {e.preventDefault(); (this.props.answer !== undefined) ?
                            this.editAnswer(this.props.answer.id, this.state.explanation, this.props.answer.likes, this.props.answer.dislikes, this.props.answer.question.id, this.props.answer?.user?.username) :
                            this.saveAnswer(this.state.explanation, this.state.likedBy, this.state.dislikedBy, this.props.questionId, this.state.username) }}>

                            <div className="form-group">
                                <label htmlFor="explanation">Description</label> {console.log(this.props.answer?.explanation)}
                                <textarea rows={this.props.numberOfRows}
                                          className={"form-control answer-text-box"}
                                          id="explanation"
                                          name="explanation"
                                          onChange={this.onChangeField}
                                          defaultValue={this.props.answer?.explanation}
                                          required></textarea>
                            </div>

                            <div>
                                {console.log(this.props)}
                            </div>

                            <button type="submit" className="btn btn-primary mr-2">Submit</button>
                            { this.props.answer === undefined &&
                            <Link type="button" className="btn btn-secondary mr-2" to={"/questions"}>Back</Link>}
                        </form>
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        if (this.props.answer !== undefined) {
            this.state.explanation = this.props.answer.explanation;
            this.state.likes = this.props.answer.likes;
            this.state.dislikes = this.props.answer.dislikes;
            console.log(this.props.answer.question?.id);
            this.state.questionId = this.props.answer.question?.id;
        }
        console.log("QID " + this.props.questionId);
    }
}

export default AnswerForm;