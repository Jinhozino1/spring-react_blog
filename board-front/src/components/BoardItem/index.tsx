import React from 'react'
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import './style.css';
import { BoardListItem } from 'types/interface';
import { useNavigate } from 'react-router-dom';
import defaultProfileImage from 'assets/image/default-profile-image.png';
import { BOARD_DETAIL_PATH, BOARD_PATH } from 'constant';


dayjs.extend(utc);
dayjs.extend(timezone);
interface Props {
    boardListItem: BoardListItem
}

// component: Board List Item 컴포넌트
export default function BoardItem({ boardListItem }: Props) {
    
    //  properties  //
    const { boardNumber, title, content, boardTitleImage } = boardListItem;
    const { favoriteCount, commentCount, viewCount } = boardListItem;
    const { writeDatetime, writerNickname, writerProfileImage } = boardListItem;

    //  function: 네비게이트 함수   //
    const navigate = useNavigate();

    //  event handler: 게시물 아이템 클릭 이벤트 처리 함수  //
    const onClickHandler = () => {
        navigate(BOARD_PATH() + '/' + BOARD_DETAIL_PATH(boardNumber));
    }

    // writeDatetime을 한국 시간 (Asia/Seoul) 기준으로 포맷팅
    const formattedWriteDatetime = writeDatetime 
      ? dayjs.utc(writeDatetime).tz('Asia/Seoul').format('YYYY.MM.DD HH:mm')
      : '';

    //  render: Board List Item 컴포넌트 랜더링
  return (
    <div className='board-list-item' onClick={onClickHandler}>
        <div className='board-list-item-main-box'>
            <div className='board-list-item-top'>
                <div className='board-list-item-profile-box'>
                    <div className='board-list-item-profile-image' 
                        style={{ backgroundImage: `url(${writerProfileImage ? writerProfileImage : defaultProfileImage})` }}></div>
                </div>
                <div className='board-list-item-write-box'>
                    <div className='board-list-item-nickname'>{writerNickname}</div>
                    <div className='board-list-item-write-datetime'>{formattedWriteDatetime}</div>
                </div>
            </div>
            <div className='board-list-item-middle'>
                <div className='board-list-item-title'>{title}</div>
                <div className='board-list-item-content'>{content}</div>
            </div>
            <div className='board-list-item-bottom'>
                <div className='board-list-item-counts'>
                    {`댓글 ${commentCount} ∙ 좋아요 ${favoriteCount} ∙ 조회수 ${viewCount}`}
                </div>
            </div>
        </div>
        {boardTitleImage && (
        <div className='board-list-item-image-box'>
            <div className='board-list-item-image'
            style={{ backgroundImage: `url(${boardTitleImage})` }}></div>
        </div>
        )} 
        
    </div>
  )
}
