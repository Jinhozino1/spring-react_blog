export default interface CommentListItem {
    nickname: string;
    profileImage: string | null;
    writerEmail: string;
    writeDatetime: string;
    content: string;
}