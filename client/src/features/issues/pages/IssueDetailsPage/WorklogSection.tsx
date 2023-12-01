import { Spinner } from "@/components/Spinner";
import {
  Worklog,
  useCreateWorklogMutation,
  useGetWorklogsQuery,
} from "../../store/WorklogApiSlice";
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
import { Button } from "@/shadcnui/ui/button";
import { isApiError } from "@/store/apiError";
import { toast } from "react-toastify";
import { Input } from "@/shadcnui/ui/input";

type worklogSectionParams = {
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

function WorklogCard({ worklog }: { worklog: Worklog }) {
  return (
    <div className="p-2 mx-1 flex flex-row space-x-2">
      <p className="bg-gray-200 font-bold p-2 self-center rounded-md">
        {worklog.story_points_spent} SP
      </p>
      <div className="flex-1">
        <div className="flex flex-row justify-between items-center">
          <p className="font-bold">{worklog.user_email}</p>
          <p className="text-gray-600 text-sm">
            {formatCreatedAt(worklog.started_at)}
          </p>
        </div>
        <p>{worklog.description}</p>
      </div>
    </div>
  );
}

function WorklogForm({ issueCode, projectCode }: worklogSectionParams) {
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      content: "",
    },
  });
  const [createWorklog, { isLoading }] = useCreateWorklogMutation();

  async function onSubmit() {
    try {
      //   await createWorklog({
      //     ...form.getValues(),
      //     issueCode,
      //     projectCode,
      //   }).unwrap();
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
                  <Input placeholder="Log work..." {...field} />
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
              {isLoading ? "Please wait..." : "Log Work"}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

export default function WorklogSection({
  issueCode,
  projectCode,
}: worklogSectionParams) {
  const {
    data: worklogs,
    error,
    isLoading,
  } = useGetWorklogsQuery({ projectCode, issueCode });
  if (isLoading) {
    return <Spinner text="Loading worklogs..." />;
  } else if (error) {
    return <span>An error occured while loading worklogs</span>;
  }
  return (
    <div className="space-y-2">
      <WorklogForm issueCode={issueCode} projectCode={projectCode} />
      <div className="divide-y-2 divide-gray-100">
        {worklogs?.map((w) => (
          <WorklogCard worklog={w} />
        ))}
      </div>
    </div>
  );
}
