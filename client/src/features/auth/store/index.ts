import { RootState } from "@/store";
import { PayloadAction, createSlice } from "@reduxjs/toolkit";

const initialState = {
  email: "",
  name: "",
  isAuthenticated: false,
};

export type SigninPayload = {
  email: string;
  name: string;
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    signin: (_, action: PayloadAction<SigninPayload>) => {
      return { ...action.payload, isAuthenticated: true };
    },
    signup: () => {},
    signout: (_) => {
      return initialState;
    },
  },
});

export const { signin, signup, signout } = authSlice.actions;
export const authReducer = authSlice.reducer;
export const authSelector = (state: RootState) => state.auth;
export const isAuthenticatedSelector = (state: RootState) =>
  state.auth.isAuthenticated;
