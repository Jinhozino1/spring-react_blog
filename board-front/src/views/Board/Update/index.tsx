import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import './style.css';
import { useBoardStore, useLoginUserStore } from 'stores';
import { useNavigate, useParams } from 'react-router-dom';
import { MAIN_PATH } from 'constant';
import { useCookies } from 'react-cookie';
import { getBoardRequest } from 'apis';
import { GetBoardResponseDto } from 'apis/response/board';
import { ResponseDto } from 'apis/response';

// component: 게시물 수정 화면
export default function BoardWrite() {

  const titleRef = useRef<HTMLTextAreaElement | null>(null);
  const contentRef = useRef<HTMLTextAreaElement | null>(null);
  const imageInputRef = useRef<HTMLInputElement |null>(null);

  const { boardNumber } = useParams();
  const { title, setTitle } = useBoardStore();
  const { content, setContent } = useBoardStore();
  const { resetBoard } = useBoardStore();
  const { loginUser } = useLoginUserStore();

  const [cookies] = useCookies();
  const [existingImageUrls, setExistingImageUrls] = useState<string[]>([]);
  const [newImageFiles, setNewImageFiles] = useState<File[]>([]);
  const [previewImageUrls, setPreviewImageUrls] = useState<string[]>([]);

  const navigate = useNavigate();

  const getBoardResponse = (responseBody: GetBoardResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const { code } = responseBody;
    if (code === 'NB') alert('존재하지 않는 게시물입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');
    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const { title, content, boardImageList, writerEmail } = responseBody as GetBoardResponseDto;
    setTitle(title);
    setContent(content);
    setExistingImageUrls(boardImageList);
    setPreviewImageUrls(boardImageList);

    if (!loginUser || loginUser.email !== writerEmail) {
      navigate(MAIN_PATH());
      return;
    }
    if (contentRef.current) {
      contentRef.current.style.height = 'auto';
      contentRef.current.style.height = `${contentRef.current.scrollHeight}px`;
    }
  }

  const onTitleChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
    setTitle(event.target.value);
    if (titleRef.current) {
      titleRef.current.style.height = 'auto';
      titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
    }
  }

  const onContentChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
    setContent(event.target.value);
    if (contentRef.current) {
      contentRef.current.style.height = 'auto';
      contentRef.current.style.height = `${contentRef.current.scrollHeight}px`;
    }
  }

  const onImageChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files || !event.target.files.length) return;

    const file = event.target.files[0];
    const previewUrl = URL.createObjectURL(file);

    setNewImageFiles(prev => [...prev, file]);
    setPreviewImageUrls(prev => [...prev, previewUrl]);

    if (imageInputRef.current) imageInputRef.current.value = '';
  }

  const onImageUploadButtonClickHandler = () => {
    if (imageInputRef.current) imageInputRef.current.click();
  }

  const onImageCloseButtonClickHandler = (deleteIndex: number) => {
    const currentPreviewUrl = previewImageUrls[deleteIndex];

    // 기존 이미지 삭제
    if (existingImageUrls.includes(currentPreviewUrl)) {
      setExistingImageUrls(prev => prev.filter(url => url !== currentPreviewUrl));
    }
    // 새 이미지 삭제
    else {
      setNewImageFiles(prev => prev.filter((_, index) => previewImageUrls.indexOf(currentPreviewUrl) !== index));
    }

    // 프리뷰 목록에서 삭제
    setPreviewImageUrls(prev => prev.filter((_, index) => index !== deleteIndex));
  }

  useEffect(() => {
    const accessToken = cookies.accessToken;
    if (!accessToken) {
      navigate(MAIN_PATH());
      return;
    }
    if (!boardNumber) return;
    getBoardRequest(boardNumber).then(getBoardResponse);
  }, [boardNumber]);

  return (
    <div id="board-update-wrapper">
      <div className="board-update-container">
        <div className="board-update-box">
          <div className="board-update-title-box">
            <textarea ref={titleRef} className="board-update-title-textarea" rows={1}
              placeholder="제목을 작성해주세요." value={title} onChange={onTitleChangeHandler} />
          </div>
          <div className="divider"></div>
          <div className="board-update-content-box">
            <textarea ref={contentRef} className="board-update-content-textarea"
              placeholder="본문을 작성해주세요." value={content} onChange={onContentChangeHandler} />
            <div className="icon-button" onClick={onImageUploadButtonClickHandler}>
              <div className="icon image-box-light-icon"></div>
            </div>
            <input ref={imageInputRef} type="file" accept="image/*" style={{ display: "none" }} onChange={onImageChangeHandler} />
          </div>
          <div className="board-update-images-box">
            {previewImageUrls.map((url, index) => (
              <div key={index} className="board-update-image-box">
                <img className="board-update-image" src={url} />
                <div className="icon-button image-close" onClick={() => onImageCloseButtonClickHandler(index)}>
                  <div className="icon close-icon"></div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}