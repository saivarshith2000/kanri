import * as z from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/shadcnui/ui/form";
import { Dialog, DialogContent, DialogTrigger } from "@/shadcnui/ui/dialog";
import { Button } from "@/shadcnui/ui/button";
import { isApiError } from "@/store/apiError";
import { toast } from "react-toastify";
import { useDropzone } from "react-dropzone";
import { useCallback, useState } from "react";
import { DialogHeader } from "@/shadcnui/ui/dialog";
import { useUploadAttachmentMutation } from "../../store/attachmentApiSlice";
import { AttachmentSectionParams } from "./AttachmentSection";
import { Spinner } from "@/components/Spinner";

type FileDropZoneParams = {
  onUpload: (file: any) => void;
  isLoading: boolean;
};

function FileDropZone({ onUpload, isLoading = false }: FileDropZoneParams) {
  const onDrop = useCallback((file: any) => {
    // Do something with the files
    onUpload(file);
  }, []);
  const { getRootProps, getInputProps } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      <div className="w-full h-[150px] bg-gray-100 rounded-md hover:bg-gray-200 hover:cursor-pointer flex items-center justify-center">
        {isLoading ? (
          <Spinner text="Uploading attachment" />
        ) : (
          <p className="text-center font-bold text-sm">
            Drag 'n' drop some files here
            <br /> or <br />
            click to select files
          </p>
        )}
      </div>
    </div>
  );
}

const MAX_FILE_SIZE = 1000000000; // 1 MB
const ALLOWED_FILE_TYPES = [
  "image/jpeg",
  "image/jpg",
  "image/png",
  "text/plain",
  "application/pdf",
];

const schema = z.object({
  attachment: z
    .instanceof(File)
    .refine((f) => f.size < MAX_FILE_SIZE, "File size cannot exceed 1 MB")
    .refine((f) => ALLOWED_FILE_TYPES.includes(f.type), "Invalid file type"),
});

type UploadAttachmentFormParams = {
  issueCode: string;
  projectCode: string;
  onSuccess: () => void;
};

function UploadAttachmentForm({
  issueCode,
  projectCode,
}: UploadAttachmentFormParams) {
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
  });
  const [uploadAttachment, { isLoading }] = useUploadAttachmentMutation();

  async function onSubmit() {
    try {
      await uploadAttachment({
        issueCode,
        projectCode,
        ...form.getValues(),
      }).unwrap();
      form.reset();
    } catch (err) {
      if (isApiError(err)) {
        console.log(err.data.timestamp);
        toast.error(err.data.errors.msg);
      } else {
        toast.error("An error occured. Please try again later.");
      }
    }
  }

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="p-2 space-y-1">
          <FormField
            control={form.control}
            name="attachment"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <FileDropZone
                    isLoading={isLoading}
                    onUpload={(file: any) => {
                      form.setValue("attachment", file);
                      onSubmit();
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </form>
      </Form>
    </div>
  );
}

export function UploadAttachmentDialog({
  issueCode,
  projectCode,
}: AttachmentSectionParams) {
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="bg-blue-600 hover:bg-blue-800" size="sm">
          Upload Attachment
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTrigger>Upload Attachment</DialogTrigger>
        </DialogHeader>
        <div>
          <UploadAttachmentForm
            projectCode={projectCode}
            issueCode={issueCode}
            onSuccess={() => setOpen(false)}
          />
        </div>
      </DialogContent>
    </Dialog>
  );
}
