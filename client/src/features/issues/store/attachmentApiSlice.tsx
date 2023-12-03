import { apiSlice } from "@/store/apiSlice";

export type Attachment = {
  content: string;
  name: string;
  type: string;
  size: number;
  created_at: string;
};

type createAttachmentPayload = {
  projectCode: string;
  issueCode: string;
  attachment: File;
};

type getAttachmentsParams = {
  projectCode: string;
  issueCode: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ["attachment"],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getAttachments: builder.query<Attachment[], getAttachmentsParams>({
        query: (params) => ({
          url: `/api/projects/${params.projectCode}/issues/${params.issueCode}/attachments`,
        }),
        providesTags: ["attachment"],
      }),
      uploadAttachment: builder.mutation<Attachment, createAttachmentPayload>({
        query: (body) => {
          var data = new FormData();
          data.append("attachment", body.attachment);
          return {
            url: `/api/projects/${body.projectCode}/issues/${body.issueCode}/attachments`,
            method: "POST",
            formData: true,
            body: data,
          };
        },
        invalidatesTags: ["attachment"],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetAttachmentsQuery, useUploadAttachmentMutation } =
  extendedApi;
