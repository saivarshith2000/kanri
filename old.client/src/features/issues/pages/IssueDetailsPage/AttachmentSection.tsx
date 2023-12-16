import { Spinner } from "@/components/Spinner";
import {
  Attachment,
  useDeleteAttachmentMutation,
  useGetAttachmentsQuery,
} from "../../store/attachmentApiSlice";
import { format, parseISO } from "date-fns";
import { UploadAttachmentDialog } from "./UploadAttachmentDialog";
import { Button } from "@/shadcnui/ui/button";
import { toast } from "react-toastify";
import { isApiError } from "@/store/apiError";

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

type AttachmentCardProps = {
  attachment: Attachment;
  issueCode: string;
  projectCode: string;
};

function AttachmentCard({
  attachment,
  issueCode,
  projectCode,
}: AttachmentCardProps) {
  const [deleteAttachment, { isLoading }] = useDeleteAttachmentMutation();

  console.log(attachment);
  const handleDelete = () => {
    try {
      deleteAttachment({ issueCode, projectCode, id: attachment.id });
    } catch (err) {
      if (isApiError(err)) {
        console.log(err.data.timestamp);
        toast.error(err.data.errors.msg);
      } else {
        toast.error("An error occured. Please try again later.");
      }
    }
  };

  return (
    <div className="p-2 mx-1 flex flex-row justify-between items-center">
      <div className="flex-1">
        <div className="flex flex-row items-center justify-between">
          <p className="font-bold">{attachment.name}</p>
          <p>{attachment.size} Bytes</p>
        </div>
        <div className="flex flex-row items-center justify-between">
          <p className="text-gray-400 text-sm">
            {formatCreatedAt(attachment.created_at)}
          </p>
          <p className="text-gray-400 text-sm">{attachment.type}</p>
        </div>
      </div>

      <a
        download={attachment.name}
        target="_blank"
        rel="noreferrer"
        href={URL.createObjectURL(
          new Blob([atob(attachment.content)], { type: attachment.type })
        )}
        style={{
          textDecoration: "inherit",
          color: "inherit",
        }}
      >
        <Button
          variant="ghost"
          className="text-blue-400 hover:text-blue-400 ml-4"
          size="sm"
        >
          Download
        </Button>
      </a>

      <Button
        variant="ghost"
        className="text-red-400 hover:text-red-400 ml-4"
        size="sm"
        onClick={handleDelete}
        disabled={isLoading}
      >
        {isLoading ? "Please wait" : "Delete"}
      </Button>
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
          <AttachmentCard
            attachment={a}
            key={a.name}
            projectCode={projectCode}
            issueCode={issueCode}
          />
        ))}
      </div>
    </div>
  );
}
