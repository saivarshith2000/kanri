import {
  BaseQueryFn,
  FetchArgs,
  FetchBaseQueryError,
  createApi,
  fetchBaseQuery,
} from "@reduxjs/toolkit/query/react";

import { FirebaseSignOutUser, getToken } from "@/firebase";
import { signout } from "@/features/auth/store";
import { SerializedError } from "@reduxjs/toolkit";

const baseQuery = fetchBaseQuery({
  baseUrl: import.meta.env.VITE_BACKEND_BASE_URL,
  prepareHeaders: async (headers, _) => {
    // This method automatically tries to refresh the token from firebase
    const token = await getToken();
    headers.set("authorization", `Bearer ${token}`);
    return headers;
  },
});

const baseQueryWithReauth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError | SerializedError
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  if (result.error) {
    // This means our refresh token has also expired, the user has to be logged out
    // FirebaseSignOutUser();
    // api.dispatch(signout);
  }
  return result;
};

export const apiSlice = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithReauth,
  endpoints: () => ({}),
});
