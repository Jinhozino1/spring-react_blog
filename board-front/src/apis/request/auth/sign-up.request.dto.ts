export default interface SignUpRequestDto {
    email: string;
    password: string;
    nickname: string;
    telNumber: string;
    address: string;
    adressDetail: string | null;
    agreedPersonal: boolean;
}

