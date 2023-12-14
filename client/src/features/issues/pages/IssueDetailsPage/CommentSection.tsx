import { Spinner } from "@/components/Spinner";
import {
  Comment,
  useCreateCommentMutation,
  useDeleteCommentMutation,
  useGetCommentsQuery,
} from "../../store/commentApiSlice";
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
import { format, parseISO } from "date-fns";
import { Textarea } from "@/shadcnui/ui/textarea";
import { Button } from "@/shadcnui/ui/button";
import { isApiError } from "@/store/apiError";
import { toast } from "react-toastify";

type commentSectionParams = {
  issueCode: string;
  projectCode: string;
};

const schema = z.object({
  content: z.string().nonempty(),
});

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

function CommentCard({
  comment,
  issueCode,
  projectCode,
}: {
  comment: Comment;
  issueCode: string;
  projectCode: string;
}) {
  const [deleteComment, { isLoading }] = useDeleteCommentMutation();

  const handleDelete = () => {
    try {
      deleteComment({ id: comment.id, issueCode, projectCode });
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
    <div className="p-2 mx-1">
      <div className="flex flex-row justify-between items-center">
        <p className="font-bold">{comment.user_email}</p>
        <p className="text-gray-600 text-sm">
          {formatCreatedAt(comment.created_at)}
        </p>
      </div>
      <div className="flex flex-row justify-between items-center">
        <p>{comment.content}</p>
        <Button
          onClick={handleDelete}
          variant="ghost"
          className="text-red-500 hover:text-red-500 hover:bg-transparent"
        >
          Delete
        </Button>
      </div>
    </div>
  );
}

function CommentForm({ issueCode, projectCode }: commentSectionParams) {
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      content: "",
    },
  });
  const [createComment, { isLoading }] = useCreateCommentMutation();

  async function onSubmit() {
    try {
      await createComment({
        ...form.getValues(),
        issueCode,
        projectCode,
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
            name="content"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Textarea placeholder="Comment on this issue..." {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <div className="flex flex-row space-x-2 items-center justify-end">
            {form.getValues().content.length > 0 ? (
              <Button variant="ghost" size="sm">
                Cancel
              </Button>
            ) : null}
            <Button
              variant="default"
              size="sm"
              className="bg-blue-600 hover:bg-blue-800"
              disabled={form.getValues().content.length === 0 || isLoading}
            >
              {isLoading ? "Please wait..." : "Comment"}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

export default function CommentSection({
  issueCode,
  projectCode,
}: commentSectionParams) {
  const {
    data: comments,
    error,
    isLoading,
  } = useGetCommentsQuery({ projectCode, issueCode });
  if (isLoading) {
    return <Spinner text="Loading comments..." />;
  } else if (error) {
    return <span>An error occured while loading comments</span>;
  }
  return (
    <div className="space-y-2">
      <CommentForm issueCode={issueCode} projectCode={projectCode} />
      <div className="divide-y-2 divide-gray-100">
        {comments?.map((c) => (
          <CommentCard
            comment={c}
            key={c.created_at}
            issueCode={issueCode}
            projectCode={projectCode}
          />
        ))}
      </div>
    </div>
  );
}
