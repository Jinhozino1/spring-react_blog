export default interface User {
    email: string;
    nickname: string;
    profileImage: string | null;
    accessToken?: string;
}