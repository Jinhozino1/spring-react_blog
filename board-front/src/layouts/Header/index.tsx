import React, { useState, ChangeEvent, useRef, KeyboardEvent, useEffect} from 'react';
import './style.css';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { AUTH_PATH, BOARD_DETAIL_PATH, BOARD_UPDATE_PATH, BOARD_WRITE_PATH, MAIN_PATH, SEARCH_PATH, USER_PATH, BOARD_PATH } from 'constant';
import { useCookies } from 'react-cookie';
import { useLoginUserStore } from 'stores';
// import BoardDetail from 'views/Board/Detail';
import { useBoardStore } from 'stores'
import { fileUploadRequest, postBoardRequest } from 'apis';
import { PostBoardRequestDto } from 'apis/request/board';
import { PostBoardResponseDto } from 'apis/response/board';
import { ResponseDto } from 'apis/response';



//  component: 헤더 레이아웃
export default function Header() {
    // state: 로그인 유저 상태
    const {loginUser, setLoginUser, resetLoginUser} = useLoginUserStore();
    
    //  state: path 상태
    const { pathname } = useLocation();

    //  state: cookie 상태
    const [cookies, setCookies] = useCookies();

    //  state: 로그인 상태
    const[isLogin, setLogin] = useState<boolean>(false);

    //  state: 인증 페이지 상태
    const[isAuthPage, setAuthPage] = useState<boolean>(false);
    //  state: 메인 페이지 상태
    const[isMainPage, setMainPage] = useState<boolean>(false);
    //  state: 검색 페이지 상태
    const[isSearchPage, setSearchPage] = useState<boolean>(false);
    //  state: 게시물 상세 페이지 상태
    const[isBoardDetailPage, setBoardDetailPage] = useState<boolean>(false);
    //  state: 게시물 작성 페이지 상태
    const[isBoardWritePage, setBoardWritePage] = useState<boolean>(false);
    //  state: 게시물 수정 페이지 상태
    const[isBoardUpdatePage, setBoardUpdatePage] = useState<boolean>(false);
    //  state: 유저 페이지 상태
    const[isUserPage, setUserPage] = useState<boolean>(false);

    

    //  function: 네비게이트 함수
    const navigate = useNavigate();

    //  event handler: 로고 클릭 이벤트 처리 함수
    const onLogoClickHandler = () => {
        navigate(MAIN_PATH());
    }

    //  component: 검색 버튼 컴포넌트
    const SearchButton = () => {

        
        // state: 검색어 버튼 입력 요소 참조 상태
        const searchButtonRef = useRef<HTMLDivElement | null>(null);

        // state: 검색 버튼 상태
        const [status, setStatus] = useState<boolean>(true);

        // state: 검색어 상태
        const [word, setWord] = useState<string>('');

        // state: 검색어 path variable 상태
        const { searchWord  } = useParams();

        //  event handler: 검색어 변경 클릭 이벤트 처리 함수
        const onSearchWordChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
            const value = event.target.value;
            setWord(value);
        };
        //  event handler: 검색어 키 이벤트 처리 함수
        const onSearchWordKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
            if(event.key !== 'Enter') return;
            if(!searchButtonRef.current) return;
            searchButtonRef.current.click();
        };

        //  event handler: 검색 버튼 클릭 이벤트 처리 함수
        const onSearchButtonClickHandler = () => {
            if(!status) {
                setStatus(!status);
                return;
            }
            navigate(SEARCH_PATH(word));
        };

        //  effect: 검색어 path variable 변경 될때 마다 실행될 함수
        useEffect(() => {
            if(searchWord) {
                setWord(searchWord);
                setStatus(true);
            }
        }, [searchWord]);

        if(!status)
        //  render: 검색 버튼 컴포넌트 렌더링 (클릭 false 상태)
        return (
            <div className='icon-button' onClick={onSearchButtonClickHandler} >
                <div className='icon search-light-icon'></div>
            </div>
        );

        //  render: 검색 버튼 컴포넌트 렌더링 (클릭 true 상태)
        return (
            <div className='header-search-input-box'>
                <input className='header-search-input' type='text' placeholder='검색어를 입력해주세요.' value={searchWord} onChange={onSearchWordChangeHandler} onKeyDown={onSearchWordKeyDownHandler}/>
                <div ref={searchButtonRef} className='icon-button' onClick={onSearchButtonClickHandler}>
                    <div className='icon search-light-icon'></div>
                </div>
            </div>
        );
    };

    //  component: 마이페이지 버튼 컴포넌트
    const MyPageButton = () => {
        //  state: userEmail path variable 상태
        const { userEmail } = useParams();

        //  event handler: 마이페이지 버튼 클릭 이벤트 처리 함수
        const onMyPageButtonClickHandler = () => {
            if(!loginUser) return;
            const { email } = loginUser;
            navigate(USER_PATH(email));
        };
        //  event handler: 마이페이지 버튼 클릭 이벤트 처리 함수
        const onSignOutPageButtonClickHandler = () => {
            resetLoginUser();
            setCookies('accessToken', '', { path: MAIN_PATH(), expires: new Date() });
            navigate(MAIN_PATH());
        };
        //  event handler: 마이페이지 버튼 클릭 이벤트 처리 함수
        const onSignInButtonClickHandler = () => {
            navigate(AUTH_PATH());
        };

        //  render: 로그아웃 버튼 컴포넌트 렌더링
        if(isLogin && userEmail === loginUser?.email)
        return <div className='white-button' onClick={onSignOutPageButtonClickHandler}>{'로그아웃'}</div>  ;

        //  render: 마이페이지 버튼 컴포넌트 렌더링
        if(isLogin)
        return <div className='white-button' onClick={onMyPageButtonClickHandler}>{'마이페이지'}</div>  ;

        //  render: 로그인 버튼 컴포넌트 렌더링
        console.log('isLogin:', isLogin);
        console.log('userEmail:', userEmail);
        console.log('loginUser:', loginUser);
        return <div className='black-button' onClick={onSignInButtonClickHandler}>{'로그인'}</div>  ;  
        
    };

    //  component: 업로드 버튼 컴포넌트
    const UploadButton = () => {

        //  state: 게시물 상태
        const { title, content, boardImageFileList, resetBoard } = useBoardStore();

        //  function: post board response 처리 함수
        const postBoardResponse = (responseBody: PostBoardResponseDto | ResponseDto | null) => {
            if (!responseBody) return;
            const { code } = responseBody;
            if (code === 'DBE') alert('데이터베이스 오류입니다.');
            if (code === 'AF' || code === 'NU') navigate(AUTH_PATH());
            if (code === 'VF') alert('제목과 내용은 필수입니다.');
            if (code !== 'SU') return;

            resetBoard();
            if (!loginUser) return;
            const { email } = loginUser;
            navigate(USER_PATH(email));
        }

        //  event handler: 업로드 버튼 클릭 이벤트 처리 함수
        const onUploadButtonClickHandler = async () => {
            const accessToken = cookies.accessToken;
            if (!accessToken) return;

            const boardImageList: string[] = [];

            for (const file of boardImageFileList) {
                const data = new FormData();
                data.append('file', file);

                const url = await fileUploadRequest(data);
                if (url) boardImageList.push(url);
            }
            const requestBody: PostBoardRequestDto = {
                title, content, boardImageList
            }
            postBoardRequest(requestBody, accessToken).then(postBoardResponse);
        }

        //  render: 업로드 불가 버튼 컴포넌트 렌더링
        if (title && content)
            return <div className='black-button' onClick={onUploadButtonClickHandler}>{'업로드'}</div>  ; 
        //  render: 업로드 버튼 컴포넌트 렌더링
        return <div className='disable-button' onClick={onUploadButtonClickHandler}>{'업로드'}</div>  ; 
}
    //  effect: path가 변경될 때 마다 실행될 함수
    useEffect(() => {
        const isAuthPage = pathname.startsWith(AUTH_PATH());
        setAuthPage(isAuthPage);
        const isMainPage = pathname === MAIN_PATH();
        setMainPage(isMainPage);
        const isSearchPage = pathname.startsWith(SEARCH_PATH(''));
        setSearchPage(isSearchPage);
        const isBoardDetailPage = pathname.startsWith(BOARD_PATH() + '/' + BOARD_DETAIL_PATH(''));
        setBoardDetailPage(isBoardDetailPage);
        const isBoardWritePage = pathname.startsWith(BOARD_PATH() + '/' + BOARD_WRITE_PATH());
        setBoardWritePage(isBoardWritePage);
        const isBoardUpdatePage = pathname.startsWith(BOARD_PATH() + '/' + BOARD_UPDATE_PATH(''));
        setBoardUpdatePage(isBoardUpdatePage);
        const isUserPage = pathname.startsWith(USER_PATH(''));
        setUserPage(isUserPage);
    }, [pathname]);

    useEffect(() => {
        setLogin(loginUser !== null);
    }, [loginUser])

    //  render: 헤더 레이아웃 렌더링
  return (
    <div id='header'>
        <div className='header-container'>
            <div className='header-left-box' onClick={onLogoClickHandler}>
                <div className='icon-box'>
                    <div className='icon logo-dark-icon'></div>
                </div>
                <div className='header-logo'>{'Jinho Board'}</div>
            </div>
            <div className='header-right-box'>
                { (isAuthPage || isMainPage || isSearchPage || isBoardDetailPage) && <SearchButton />}
                { (isMainPage || isSearchPage || isBoardDetailPage || isUserPage) && <MyPageButton />}
                { (isBoardWritePage || isBoardUpdatePage) && <UploadButton />}
            </div>
        </div>
    </div>
  )
}
