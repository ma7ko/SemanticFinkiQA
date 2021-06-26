import '../QuestionTerm/questions.css';
import React, {useEffect, Component} from 'react';
import {Link, useHistory} from 'react-router-dom';
//import tags from "../../Tags/TagsList/tags";
//import FinkiQAService from "../../../repository/finkiqaRepository";
import {faCarCrash, faPaperPlane, faTags, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import QuestionService from '../../../services/questionService';
import './questionForm.css';
import Pagination from "../../Pagination/pagination";
//import Pagination from "../../Pagination/pagination";

class QuestionsForm extends Component {

    constructor(props) {
        super(props);
        this.onChangeField = this.onChangeField.bind(this);
        this.getQuestion = this.getQuestion.bind(this);
        this.saveQuestion = this.saveQuestion.bind(this);
        this.onFormSubmit = this.onFormSubmit.bind(this);
        this.setCurrentPage = this.setCurrentPage.bind(this);
        this.removeTag = this.removeTag.bind(this);

        if (this.props?.question?.tags != undefined) {
            this.state = {
                questions: [],
                currentQuestion: {},
                id: "",
                title: "",
                description: "",
                username: this.props.currentUser.id,
                tags: [],
                lastQuestion: {},
                name: '',
                previousUri: '',
                newTags: [],
                currentPage: 1,
                firstIndex: 0,
                lastIndex: 2,
                tagsPerPage: 2,
                removed: 0
            }
        }
    }

    setCurrentPage(page) {
        console.log(page);
        this.state.currentPage = page;
        this.state.firstIndex = page * 2 - 2;
        this.setState({
            currentPage: page,
            lastIndex: page * 2,
            firstIndex: page * 2 - 2,
        });
        console.log(this.state);
    }

    forceUpdate(callback) {
        super.forceUpdate(callback);
    }

    onChangeField(e) {
        this.setState({
            [e.target.name]: e.target.value.trim()
        });
    }


    getQuestion(questionId) {
        QuestionService.getQuestion(questionId)
            .then((response) => {
                this.setState({
                    currentQuestion: response.data
                })
            })
    }

    editQuestion(e) {
        e.preventDefault();
        QuestionService.deleteQuestion(this.props.questionId).then((response) => {
            QuestionService.addQuestion(this.state.title,
                this.state.description,
                this.props?.currentUser.id,
                this.state.tags).then((response) => {
                this.props.props.history.push("/questions");
            });
        });
    }

    saveQuestion(e) {
        e.preventDefault();
        QuestionService.addQuestion(this.state.title,
            this.state.description,
            this.props?.currentUser.id,
            this.state.newTags).then((response) => {
            this.props.props.history.push("/questions");
        });

    }

    onFormSubmit(e) {
        e.preventDefault();
        this.setState({
            tags: this.state?.tags?.concat(this.state?.name?.substring(0, 19)),
            newTags: this.state.newTags === undefined? [].concat(this.state?.name?.substring(0, 19)) : this.state?.newTags?.concat(this.state?.name?.substring(0, 19))
        })
    }

    removeTag(e) {
        for (let i in this.state.tags) {
            console.log(this.state.tags?.[i].valueOf())
            console.log(e.target.parentNode.parentNode.parentNode.innerText)
            if (this.state.tags?.[i].trim() == e.target.parentNode.parentNode.parentNode.innerText.trim()) {
                this.state.tags?.splice(i, 1)
                this.setState({
                    removed: this.state.removed+1
                })
            } if (this.state.newTags?.[i].trim() == e.target.parentNode.parentNode.parentNode.innerText.trim()) {
                this.state.newTags?.splice(i, 1)

                this.setState({
                    removed: this.state.removed+1
                })
            }
        }
    }


    render() {
        return(
            <div className="container">
                <div className="row m-5">
                    <div className="col-md-4">
                        <form onSubmit={(e) => { this.props.questionId !== "" ? this.editQuestion(e) : this.saveQuestion(e)}}>
                            <div className="form-group">
                                <label htmlFor="title">Question Title</label>
                                <input type="text"
                                       className={"form-control"}
                                       id="title"
                                       name="title"
                                       onChange={this.onChangeField}
                                       defaultValue={this.props.questionId === "" ? "" : this.state?.currentQuestion.title}
                                       required/>
                            </div> {console.log(this.state)}
                            <div className="form-group">
                                <label htmlFor="description">Description</label>
                                <textarea rows={"8"}
                                          cols={"80"}
                                          className={"form-control"}
                                          id="description"
                                          name="description"
                                          onChange={this.onChangeField}
                                          defaultValue={this.props.questionId === "" ? "" : this.state?.currentQuestion?.description}
                                          required></textarea>
                            </div>
                            <div>{console.log("This is the state: ")} {console.log(this.props)}</div>




                            <button type="submit" className="btn btn-success mt-2 mr-2">Submit</button>
                            <Link type="button" className="btn btn-secondary ml-2 mt-2" to={"/questions"}>Back</Link>
                        </form>

                        <div>
                        </div>
                    </div>

                    <div className={"col-md-4 mt-4"}>
                        <div className={"container"}>
                            <div className={"card tags-card"}>
                                <div className={"card-header text-dark"}>Added tags:
                                </div>
                                <div className={"card-body not-found"}>
                                <div className={"fit-c not-found"}>
                                    {console.log(this.state?.firstIndex)}
                                    {console.log(this.state?.tags?.slice(this.state.firstIndex, this.state.lastIndex).toString())}
                                    {console.log(this.state?.newTags)}


                                    {((this.props.questionId !== "" && this.state?.tags?.length === 0) || (this.props.questionId === "" && (this.state?.newTags === undefined || this.state?.newTags?.length === 0))) && <div className={"text-center not-found"}> <FontAwesomeIcon size='3x' icon={faTags}/>  <p>You do not have any selected tags yet</p></div> }

                                    {this.props.questionId !== "" && this.state?.tags?.length > 0 &&
                                    <div className={"not-found"}>
                                        {
                                            this.state?.tags?.slice(this.state.firstIndex, this.state.lastIndex).map((term) => {
                                                return <div key={term}> <h3><span className={"badge badge-info"}>{term}<span className={"tag-remove-hidden text-info"} onClick={(e) => {this.removeTag(e)
                                                }}> <FontAwesomeIcon onMouseOut={(e) => {e.target.classList.remove("text-light");}} onMouseOver={(e) => {console.log(e); e.target.classList.add("text-light");}} icon={faTimesCircle}/> </span> </span></h3></div>
                                            })

                                        }
                                        <div className={'m-5'}>
                                            { this.state.tags?.length > 0 && <Pagination paginate={this.setCurrentPage} totalPosts={this.state.tags?.length} postsPerPage={this.state.tagsPerPage} currentPage={this.state.currentPage} mainPage={false}/>}
                                        </div>
                                    </div>}
                                    {console.log(this.state?.newTags?.slice(this.state.firstIndex, this.state.lastIndex))}


                                        {this.props.questionId === "" && this.state?.newTags?.length > 0 &&
                                        <div className={'not-found'}>
                                            {
                                                this.state?.newTags?.slice(this.state.firstIndex, this.state.lastIndex).map((term) => {
                                                    return <div key={term}> <h3><span className={"badge badge-info"}>{term}<span className={"tag-remove-hidden text-info"} onClick={(e) => { this.removeTag(e)
                                                    }}> <FontAwesomeIcon onMouseOut={(e) => {e.target.classList.remove("text-light");}} onMouseOver={(e) => {console.log(e); e.target.classList.add("text-light");}} icon={faTimesCircle}/> </span> </span></h3></div>
                                                })
                                            }
                                            <div className={'m-5'}>
                                                { this.state.newTags?.length > 0 && <Pagination paginate={this.setCurrentPage} totalPosts={this.state.newTags?.length} postsPerPage={this.state.tagsPerPage} currentPage={this.state.currentPage} mainPage={false}/>}
                                            </div>
                                        </div>}

                                </div>


                                        {/*{this.props.question?.tags !== undefined && this.props.question?.tags?.length > 5 && <div><Pagination mainPage={false} totalPosts={this.state.currentQuestion?.tags?.length} postsPerPage={this.state.postsPerPage} currentPage={this.state.currentPage} paginate={this.setCurrentPage}/></div>}*/}
                                    {/*</div>*/}
                                    {/*<div> {this.props.question?.tags !== undefined &&*/}
                                    {/*<div className={'boxes'}>*/}
                                    {/*    {*/}
                                    {/*        this.props.question?.tags?.slice(this.state.indexOfFirstPost,this.state.indexOfLastPost).map((term) => {*/}
                                    {/*            return <Link id={`${term.id}to`} to={"#"} onClick={(e) => {this.removeTag(e, term.id)}}> <h3><span id="tag" className={"badge badge-info"}>{term}</span></h3> </Link>*/}
                                    {/*        })*/}
                                    {/*    }*/}
                                    {/*</div> }*/}
                                        {/*{this.state.tags?.length > 5 && <div><Pagination mainPage={false} totalPosts={this.state.tags?.length} postsPerPage={this.state.postsPerPage} currentPage={this.state.currentPage} paginate={this.setCurrentPage}/></div>}*/}


                                    {console.log(this.props.question?.length)}


                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={"col-md-4 mt-4"}>
                        <div className={"container"}>
                            <div className={"card tags-card tag-add"}>



                                <form onSubmit={this.onFormSubmit} className={"m-5"}>

                                    <div className="form-group tag-add">
                                        <label htmlFor="name">Enter Tag name</label>
                                        <input type="text"
                                               className={"form-control"}
                                               id="name"
                                               name="name"
                                               onChange={this.onChangeField}
                                               required/>
                                        <button type="submit" className="btn btn-primary">Submit</button>
                                    </div>

                                </form>                                                             {/*<div className={"card-body"}>*/}
                                {/*    <p className={"hide-tag-ids"} id={"tags"}></p>*/}


                                {/*    {*/}
                                {/*        this.state?.searchedTags?.length === 0 && (document.getElementById("search-input")?.value.length > 0) && <div className={"text-center not-found"}>*/}
                                {/*            <FontAwesomeIcon size='3x' icon={faCarCrash}/>*/}
                                {/*            <p className={"m-2"}>The tag you searched for is not found</p>*/}
                                {/*        </div>*/}
                                {/*    }*/}

                                {/*    <div>*/}
                                {/*        {console.log(document.getElementById("search-input")?.value)}*/}
                                {/*        {console.log( this.state?.searchedTags?.length === 0)}*/}
                                {/*        { this.state?.searchedTags?.length === 0 && <div className={'boxes-search'}> {   this.state?.searchedTags?.length === 0 && (document.getElementById("search-input")?.value.length === 0 || document.getElementById("search-input")?.value === undefined) &&*/}
                                {/*        this.props?.tags?.slice(this.state.indexOfFirstPostSearch,this.state.indexOfLastPostSearch).map((term) => {*/}
                                {/*            // if (props.question !== undefined && props.question.tags !== undefined) {*/}
                                {/*            //     for (let ta = 0; ta < props.question.tags.length; ta++) {*/}
                                {/*            //         console.log(ta);*/}
                                {/*            //         console.log(props.question.tags[ta]);*/}
                                {/*            //         if (props.question.tags[ta].id !== term.id) {*/}
                                {/*            //             return <Link id={`${term.id}from`} to={"#"} onClick={(e) => {addTag(e,term);}}> <h3><span id="tag" className={"badge badge-primary"}>{term.name}</span></h3> </Link>*/}
                                {/*            //         }*/}
                                {/*            //         else {*/}
                                {/*            //             return <Link className={"hide-tag-ids"} id={`${term.id}from`} to={"#"} onClick={(e) => {addTag(e,term);}}> <h3><span id="tag" className={"badge badge-primary"}>{term.name}</span></h3> </Link>*/}
                                {/*            //         }*/}
                                {/*            //     }*/}
                                {/*            // }*/}
                                {/*            return <Link id={`${term.id}from`} to={"#"} onClick={(e) => {this.addTag(e, term.id)}}> <h3><span id="tag" className={"badge badge-info"}>{term.name}</span></h3> </Link>*/}
                                {/*        })*/}
                                {/*        } </div>}*/}
                                {/*        { this.state?.searchedTags?.length === 0 && (document.getElementById("search-input")?.value.length === 0 || document.getElementById("search-input")?.value === undefined) && this.props.tags?.length > 5 && <div className={'m-4 fixed'}><Pagination mainPage={false} totalPosts={this.props.tags?.length} postsPerPage={this.state.postsPerPageSearch} currentPage={this.state.currentPageSearch} paginate={this.setCurrentPageSearch}/></div>}*/}

                                {/*    </div>*/}
                                {/*    <div> {this.state?.searchedTags?.length > 0  && <div className={'boxes-search'}> {*/}
                                {/*        this.state?.searchedTags?.slice(this.state.indexOfFirstPostSearch,this.state.indexOfLastPostSearch).map((term) => {*/}
                                {/*            return <Link id={`${term.id}from`} to={"#"} onClick={(e) => {this.addTag(e, term.id)}}> <h3><span id="tag" className={"badge badge-info"}>{term.name}</span></h3> </Link>*/}
                                {/*        }) } </div>}*/}

                                {/*        {this.state?.searchedTags?.length !== 0 && this.state?.searchedTags?.length > 5 && <div className={'m-4 fixed'}><Pagination mainPage={false} totalPosts={this.state.searchedTags?.length} postsPerPage={this.state.postsPerPageSearch} currentPage={this.state.currentPageSearch} paginate={this.setCurrentPageSearch}/></div>}*/}
                                {/*    </div>*/}
                                {/*</div>*/}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }


    componentDidMount() {
        if (this.props?.questionId !== undefined) {
            QuestionService.getQuestion(this.props?.questionId).then((response) => {
                if (response) {
                    this.setState({
                        title: response.data.title,
                        description: response.data.description,
                        tags: this.props?.questionId === "" ? [] : response.data.tags,
                        previousUri: response.data.uri,
                        currentQuestion: response.data,
                        currentPage: 1,
                        firstIndex: 0,
                        lastIndex: 2,
                        tagsPerPage: 2
                    })
                }
            })
        }
        // console.log("URL")
        // console.log(this.props);
        // if (this.props.props.match?.params?.id !== "new") {
        //     this.getQuestion(this.props.props.match?.url);
        // }
    }

}

export default QuestionsForm;