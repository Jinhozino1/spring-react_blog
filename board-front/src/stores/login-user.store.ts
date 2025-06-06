import { User } from 'types/interface';
import { create } from 'zustand';

interface LoginUserStore {
  loginUser: User | null;
  accessToken: string;
  setLoginUser: (loginUser: User) => void;
  resetLoginUser: () => void;
  setAccessToken: (accessToken: string) => void;
  clearAccessToken: () => void;
}

const useLoginUserStore = create<LoginUserStore>(set => ({
  loginUser: null,
  accessToken: '',
  setLoginUser: loginUser => set(state => ({...state, loginUser })),
  resetLoginUser: () => set(state => ({...state, loginUser: null })),
  setAccessToken: (accessToken) => set({ accessToken }),
  clearAccessToken: () => set({ accessToken: '' }),
}));

export default useLoginUserStore;