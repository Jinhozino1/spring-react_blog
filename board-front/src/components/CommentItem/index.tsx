import React, { useState } from 'react';
import './style.css';
import { Board, CommentListItem } from 'types/interface';
import defaultProfileImage from 'assets/image/default-profile-image.png';
import dayjs from 'dayjs';
import timezone from 'dayjs/plugin/timezone';
import { Navigate, useNavigate } from 'react-router-dom';
import { USER_PATH } from 'constant';

dayjs.extend(timezone);

interface Props {
    commentListItem: CommentListItem;
}

//  component: Comment List Item 컴포넌트
export default function CommentItem({ commentListItem }: Props) {

    //  state: properties
    const { nickname, profileImage, writeDatetime, content } = commentListItem;


    //  event handler: 닉네임 클릭 이벤트 처리
    const onNicknameClickHandler = () => {
        navigate(USER_PATH(commentListItem.writerEmail));
    }



    // function: 네비게이트 함수
    const navigate = useNavigate();

    //  function: 직성일 경과시간 함수
    const getElapsdTime = () => {
        const now = dayjs().tz('Asia/Seoul');  // 현재 시간도 KST 기준
        const writeTime = dayjs.tz(commentListItem.writeDatetime, 'Asia/Seoul');

        const gap = now.diff(writeTime, 's');
        if (gap < 60) return `${gap}초 전`;
        if (gap < 3600) return `${Math.floor(gap / 60)}분 전`;
        if (gap < 86400) return `${Math.floor(gap / 3600)}시간 전`;
        return `${Math.floor(gap / 86400)}일 전`;
    }

    // function: 작성일 포멧 변경 함수
    const getWriteDatetimeFormat = () => {
        if (!commentListItem) return '';
        const date = dayjs.tz(commentListItem.writeDatetime, 'Asia/Seoul');
        return date.format('YYYY. MM. DD ∙ HH:mm');
      }

      
    //  render: Comment List Item 렌더링
  return (
    <div className='comment-list-item'>
        <div className='comment-list-item-top'>
            <div className='comment-list-item-profile-box'>
                <div className='comment-list-item-profile-image' style={{ backgroundImage: `url(${profileImage ? profileImage : defaultProfileImage})` }}></div>
            </div>
            <div className='comment-list-item-nickname' onClick={onNicknameClickHandler}>{nickname}</div>
            {/* <div className='comment-list-item-divider'>{'\|'}</div> */}
            <div className='comment-list-item-date-box'>
                <div className='comment-list-item-day'>{getWriteDatetimeFormat()}</div>
                <div className='comment-list-item-space'>{'ㅁ'}</div>
                <div className='comment-list-item-divider'>{'\|'}</div>
                <div className='comment-list-item-space'>{'ㅁ'}</div>
                <div className='comment-list-item-time'>{getElapsdTime()}</div>
            </div>
            
        </div>
        <div className='comment-list-item-main'>
            <div className='comment-list-item-content'>{content}</div>
        </div>
    </div>
  )
}
