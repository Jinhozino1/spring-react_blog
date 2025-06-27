import React, { useEffect, useState } from 'react';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import Main from 'views/Main'
import Authentication from 'views/Authentication';
import Search from 'views/Search';
import UserP from 'views/User';
import BoardDetail from 'views/Board/Detail';
import BoardWrite from 'views/Board/Write';
import BoardUpdate from 'views/Board/Update';
import Container from 'layouts/Container';
import { AUTH_PATH } from 'constant';
import { SEARCH_PATH } from 'constant';
import { USER_PATH } from 'constant';
import { MAIN_PATH } from 'constant';
import { BOARD_PATH } from 'constant';
import { BOARD_WRITE_PATH } from 'constant';
import { BOARD_DETAIL_PATH } from 'constant';
import { BOARD_UPDATE_PATH } from 'constant';
import { useCookies } from 'react-cookie';
import { useLoginUserStore } from 'stores';
import { GetSignInUserResponseDto } from 'apis/response/user';
import { ResponseDto } from 'apis/response';
import { User } from 'types/interface';
import { getSignInUserRequest } from 'apis';
import axios from 'axios';
import { top3BoardListMock } from 'mocks';
import Top3Item from 'components/Top3Item';


//  component: Application 컴포넌트 
function App() {

  //  state: 로그인 유저 전역 상태
  const { setLoginUser, resetLoginUser } = useLoginUserStore();
  //  state: cookie 상태 
  const [cookies, setCookie] = useCookies();

  //  function: get sign in user response 처리 함수
  const getSignInUserResponse = (responseBody: GetSignInUserResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const { code } = responseBody;
    if (code === 'AF' || code === 'NU' || code === 'DBE') {
      resetLoginUser();
      return ;
    }
    const loginUser: User = { ...responseBody as GetSignInUserResponseDto };
    setLoginUser(loginUser);
  }
  //  effect: accessToken cookie 값이 변경될 떄 마다 실행할 함수
  useEffect(() => {
    console.log("accessToken 감지", cookies.accessToken);

    getSignInUserRequest()
      .then(getSignInUserResponse)
      .catch(() => resetLoginUser());
  }, []);

  // useEffect(() => {
  //   const fetchUser = async () => {
  //     if (!cookies.accessToken) {
  //       resetLoginUser(); // 로그인 유저 상태 초기화
  //       return;
  //     }
  
  //     const response = await getSignInUserRequest();
  
  //     if (!response || 'code' in response) {
  //       resetLoginUser(); // 토큰이 만료됐거나 오류인 경우
  //       return;
  //     }
  
  //     getSignInUserResponse(response); // 정상 응답이면 유저 정보 업데이트
  //   };
  
  //   fetchUser();
  // }, [cookies.accessToken]);

  //  render: Application 컴포넌트 렌더링 
  //  description: 메인 화면 : '/' - Main 
  //  description: 로그인 + 회원가입 화면 : '/auth' - Authentication
  //  description: 검색화면 : /search/:word' - Search
  //  description: 게시물 상세보기 : '/board/detail/:boardNumber' - BoardDetail
  //  description: 게시물 작성하기 : '/board/write' - BoardWrite
  //  description: 게시물 수정하기 : '/board/update/:boardNumber' - BoardUpdate
  //  description: 유저 페이지 :  '/user/:userEmail' - User
  return (
    <Routes>
      <Route element={<Container />}>
        <Route path={MAIN_PATH()} element={<Main />} />
        <Route path={AUTH_PATH()} element={<Authentication />} />
        <Route path={SEARCH_PATH(`:searchWord`)} element={<Search />} />
        <Route path={USER_PATH(`:userEmail`)} element={<UserP />} />
        <Route path={BOARD_PATH()}> 
          <Route path={BOARD_WRITE_PATH()} element={<BoardWrite />} />  
          <Route path={BOARD_DETAIL_PATH(':boardNumber')} element={<BoardDetail />} />
          <Route path={BOARD_UPDATE_PATH(':boardNumber')} element={<BoardUpdate />} />
        </Route>
        <Route path='*' element={<h1>404 Not Found</h1>} />
      </Route>
    </Routes>
    // <>
    //   <div style={ { display: 'flex', justifyContent: 'center', gap: '24px'} }>
    //     {top3BoardListMock.map(top3ListItem => <Top3Item top3ListItem={top3ListItem } />)}
    //   </div>
    // </>
  );
}

export default App;
