import { apiSlice } from '@/store/apiSlice';

export type SignupPayload = {
  display_name: string;
  email: string;
  password: string;
};

const extendedApi = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    signup: builder.mutation<void, SignupPayload>({
      query: (body) => ({
        url: '/api/accounts/register',
        method: 'POST',
        body,
      }),
    }),
  }),
  overrideExisting: false,
});

export const { useSignupMutation } = extendedApi;
