import { useEffect, useState } from "react";

const usePagination = <T>(countPerPage: number) => {
    //  state : 전체 객체 리스트 상태
    const [totalList, setTotalList] = useState<T[]>([]);
    //  state : 보여줄 객체 리스트 상태
    const [viewList, setViewList] = useState<T[]>([]);
    //  state : 현재 페이지 변호 상태
    const [currentPage, setCurrentPage] = useState<number>(1);

    //  state : 현재 페이지 번호 리스트 상태
    const [totalPageList, setTotalPageList] = useState<number[]>([1]);
    //  state : 보여줄 페이지 번호 리스트 상태
    const [viewPageList, setViewPageList] = useState<number[]>([1]);
    //  state : 현재 섹션 상태
    const [currentSection, setCurrentSection] = useState<number>(1);

    //  state : 전체 섹션 상태
    const [totalSection, setTotalSection] = useState<number>(1);

    // //  function: 보여줄 객체 리스트 호출 함수
    // const setView = () => {
    //     const FIRST_INDEX = countPerPage * (currentPage - 1);
    //     const LAST_INDEX = totalList.length > countPerPage * currentPage ? countPerPage * currentPage : totalList.length;
    //     const viewList = totalList.slice(FIRST_INDEX, LAST_INDEX);
    //     setViewList(viewList);
    // };
    // //  function: 보여줄 페이지 리스트 호출 함수
    // const setViewPage = () => {
    //     const FIRST_INDEX = 10 * (currentSection - 1);
    //     const LAST_INDEX = totalPageList.length > 10 * currentSection ? 10 * currentSection : totalPageList.length;
    //     const viewPageList = totalPageList.slice(FIRST_INDEX, LAST_INDEX);
    //     setViewPageList(viewPageList);
    // };

        // 현재 페이지가 바뀔 때 보여줄 리스트 갱신
    useEffect(() => {
        const startIndex = (currentPage - 1) * countPerPage;
        const endIndex = startIndex + countPerPage;
        setViewList(totalList.slice(startIndex, endIndex));
    }, [currentPage, totalList]);

    // 현재 섹션이 바뀔 때 보여줄 페이지 리스트 갱신
    useEffect(() => {
        const startIndex = (currentSection - 1) * 10;
        const endIndex = startIndex + 10;
        setViewPageList(totalPageList.slice(startIndex, endIndex));
    }, [currentSection, totalPageList]);
        useEffect(() => {
        const totalItemCount = totalList.length;

        const totalPage = Math.ceil(totalItemCount / countPerPage);
        const totalPageList = Array.from({ length: totalPage }, (_, i) => i + 1);
        setTotalPageList(totalPageList);

        const totalSection = Math.ceil(totalPage / 10);
        setTotalSection(totalSection);

        // 페이지와 섹션 초기화
        setCurrentPage(1);
        setCurrentSection(1);
    }, [totalList]);

    // //  effect: total list가 변경될 때마다 실행할 작업
    // useEffect(() => {
    //     const totalPage = Math.ceil(totalList.length / countPerPage);
    //     const totalPageList: number[] = [];
    //     for (let page = 1; page <= totalPage; page++) totalPageList.push(page);
    //     setTotalPageList(totalPageList);
        
    //     const totalSection = Math.ceil(totalList.length / (countPerPage * 10));
    //     setTotalSection(totalSection);

    //     setCurrentPage(1);
    //     setCurrentSection(1);

    //     // setView();
    //     // setViewPage();
    // }, [totalList]);
    // //  effect: current page가 변경될 때마다 실행
    // useEffect(setView, [currentPage]);
    // //  effect: current section이 변경될 때마다 실행
    // useEffect(setViewPage, [currentSection]);

    return {
        currentPage,
        setCurrentPage,
        currentSection,
        setCurrentSection,
        viewList,
        viewPageList,
        totalSection,
        setTotalList
    };
};

export default usePagination;