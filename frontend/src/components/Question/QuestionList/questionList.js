import React, {Component} from 'react';
import QuestionService from "../../../services/questionService";
import QuestionTerm from "../QuestionTerm/questionTerm";
import '../QuestionTerm/questions.css';
import Pagination from "../../Pagination/pagination";

class QuestionList extends Component {
    constructor(props) {
        super(props);

        this.listQuestions = this.listQuestions.bind(this);
        this.deleteQuestion = this.deleteQuestion.bind(this);
        this.likeQuestion = this.likeQuestion.bind(this);
        this.dislikeQuestion = this.dislikeQuestion.bind(this);
        this.setCurrentPage = this.setCurrentPage.bind(this);

        this.state = {
            questions: [],
            firstIndex: 0,
            lastIndex: 2,
            postsPerPage: 2,
            currentPage: 1
        }
    }

    setCurrentPage(page) {
        this.state.firstIndex = page * 2;
        this.state.currentPage = page;
        this.setState({
            firstIndex: (page * 2) - 2,
            lastIndex: page * 2
        });
        console.log(this.state)
        window.scrollTo(0, 0);
    }


    listQuestions() {
        QuestionService.getQuestions().then((response) =>{
            console.log(response);
            this.setState({
                questions: response.data
            });}
        );
    }

    deleteQuestion(questionId) {
        QuestionService.deleteQuestion(questionId).then((response) => {
            this.listQuestions();
        });
    }

    likeQuestion(question, user) {
        QuestionService.likeQuestion(question, user).then((response) => {
            this.listQuestions()
        })
    }

    dislikeQuestion(question, user) {
        QuestionService.dislikeQuestion(question, user).then((response) => {
            this.listQuestions();
        })
    }

    render() {
        return (
            <div className={"container mt-5"}>
                <div className={"row"}>
                    <div className={"col-md-1"}></div>
                    <div className={"col-md-9"}>
                        <div>{console.log(this.state.questions.slice(this.state.firstIndex, this.state.lastIndex))}
                            {
                                this.state.questions?.slice(this.state.firstIndex, this.state.lastIndex).map((term) => {
                                    return (
                                        <QuestionTerm key={JSON.parse(term)?.["@id"]}
                                                      term={JSON.parse(term)}
                                                      currentUser={this.props.currentUser}
                                                      deleteQuestion={this.deleteQuestion}
                                                      getQuestion={this.props.getQuestion}
                                                      setProfileUser={this.props.setProfileUser}
                                                      likeQuestion={this.likeQuestion}
                                                      dislikeQuestion={this.dislikeQuestion}
                                                      resetCurrentQuestion={this.props.resetCurrentQuestion}/>
                                    );
                                })
                            }</div>
                    </div>
                    <div className={"col-md-1"}></div>
                </div>
                <div className={'row'}>
                    <div className={'col-md-12'}>
                        <div className={'m-5'}>
                            { this.state.questions?.length > 0 && <Pagination paginate={this.setCurrentPage} totalPosts={this.state.questions.length} postsPerPage={this.state.postsPerPage} currentPage={this.state.currentPage} mainPage={true}/>}
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        this.listQuestions();
    }
}

export default QuestionList;