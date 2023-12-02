import { Spinner } from "@/components/Spinner";
import {
  Attachment,
  useGetAttachmentsQuery,
} from "../../store/attachmentApiSlice";
import { format, parseISO } from "date-fns";
import { UploadAttachmentDialog } from "./UploadAttachmentDialog";

export type AttachmentSectionParams = {
  issueCode: string;
  projectCode: string;
};

function formatCreatedAt(date: string) {
  try {
    const parsedDate = parseISO(date);
    const timeString = format(parsedDate, "h:mm a");
    const dateString = format(parsedDate, "do/MMM/yyyy");
    return `At ${timeString} On ${dateString}`;
  } catch (error) {
    return "";
  }
}

function AttachmentCard({ attachment }: { attachment: Attachment }) {
  return (
    <div className="p-2 mx-1">
      <p className="font-bold">{attachment.name}</p>
    </div>
  );
}

export default function AttachmentSection({
  issueCode,
  projectCode,
}: AttachmentSectionParams) {
  const {
    data: attachments,
    error,
    isLoading,
  } = useGetAttachmentsQuery({ projectCode, issueCode });
  if (isLoading) {
    return <Spinner text="Loading attachments..." />;
  } else if (error) {
    return <span>An error occured while loading attachments</span>;
  }
  return (
    <div className="space-y-2">
      <div className="flex flex-row items-center justify-between mx-2">
        <p className="text-gray-400 text-sm">
          Attachments related to this issue appear here
        </p>
        <UploadAttachmentDialog
          issueCode={issueCode}
          projectCode={projectCode}
        />
      </div>
      <div className="divide-y-2 divide-gray-100">
        {attachments?.map((a) => (
          <AttachmentCard attachment={a} key={a.name} />
        ))}
      </div>
    </div>
  );
}
