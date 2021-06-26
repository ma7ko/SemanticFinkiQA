import React from 'react'
import {
    faAngleDoubleLeft,
    faAngleDoubleRight,
    faChevronCircleLeft,
    faChevronCircleRight
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './pagination.css';

const Pagination = ({postsPerPage, totalPosts, currentPage, paginate, mainPage}) => {

    const pageNumbers = [];

    for(let i=1; i<=Math.ceil(totalPosts/postsPerPage); i++){
        pageNumbers.push(i);
    }

    return (<nav>
        {/*<ul> {console.log(postsPerPage)}*/}
        {/*    {console.log(totalPosts)}*/}
        {/*    {pageNumbers.map((number) => {*/}
        {/*        return <li>*/}
        {/*            <a onClick={() => {paginate(number)}} href={"#"}>{number}</a>*/}
        {/*        </li>*/}
        {/*    })}*/}
        {/*</ul>*/}

        <div className={'container'}>
            <div className={'row'}>
                <div className={'col-md-4'}></div>
                <div className={'col-md-4 cont1'}>
                    { mainPage && <FontAwesomeIcon className={'arrows m-2'} onClick={() => paginate(1)} icon={faAngleDoubleLeft}/>}
                    <FontAwesomeIcon className={'arrows m-2'} onClick={() => {(currentPage > 1) ? paginate(currentPage-1) : paginate(currentPage) }} icon={faChevronCircleLeft}/>
                    <FontAwesomeIcon className={'arrows m-2'} onClick={() => {(currentPage < pageNumbers.length) ? paginate(currentPage+1) : paginate(currentPage)}} icon={faChevronCircleRight}/>
                    { mainPage && <FontAwesomeIcon className={'arrows m-2'} onClick={() => paginate(pageNumbers.length)} icon={faAngleDoubleRight}/> }
                </div>
                <div className={'col-md-4'}></div>
            </div>
        </div>
    </nav>)
}

export default Pagination;