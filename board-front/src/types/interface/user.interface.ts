export default interface User {
    email: string;
    nickname: string;
    profileImage: string | null;
    accessToken?: string; // ❗️옵셔널로 해두면 유연하게 사용 가능
}