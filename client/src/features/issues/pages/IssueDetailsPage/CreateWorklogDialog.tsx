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
import { Input } from "@/shadcnui/ui/input";
import DateTimePicker from "@/components/datetimepicker";
import { useCreateWorklogMutation } from "../../store/WorklogApiSlice";
import { worklogSectionParams } from "./WorklogSection";
import { useState } from "react";
import { DialogHeader } from "@/shadcnui/ui/dialog";

const schema = z.object({
  description: z.string().nonempty(),
  story_points_spent: z.coerce.number().min(0.5),
  started_at: z.date(),
});

type CreateWorklogFormParams = {
  issueCode: string;
  projectCode: string;
  onSuccess: () => void;
};

function CreateWorklogForm({
  issueCode,
  projectCode,
  onSuccess,
}: CreateWorklogFormParams) {
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      description: "",
      started_at: new Date(),
      story_points_spent: 0,
    },
  });
  const [createWorklog, { isLoading }] = useCreateWorklogMutation();

  async function onSubmit() {
    try {
      await createWorklog({
        ...form.getValues(),
        issueCode,
        projectCode,
      }).unwrap();
      form.reset();
      onSuccess();
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
        <form onSubmit={form.handleSubmit(onSubmit)} className="p-2 space-y-4">
          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input placeholder="Describe your work" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="story_points_spent"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <Input
                    placeholder="Story Points Spent"
                    {...field}
                    type="number"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="started_at"
            render={({ field }) => (
              <FormItem>
                <FormControl>
                  <DateTimePicker
                    allowFutureDates={false}
                    date={field.value}
                    setDate={field.onChange}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <div className="flex flex-row space-x-2 items-center justify-end">
            {form.getValues().description.length > 0 ? (
              <Button variant="ghost" size="sm">
                Cancel
              </Button>
            ) : null}
            <Button
              variant="default"
              size="sm"
              className="bg-blue-600 hover:bg-blue-800"
              disabled={!form.formState.isValid}
            >
              {isLoading ? "Please wait..." : "Log Work"}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

export function CreateWorklogDialog({
  issueCode,
  projectCode,
}: worklogSectionParams) {
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="bg-blue-600 hover:bg-blue-800" size="sm">
          Log Work
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTrigger>Log Work</DialogTrigger>
        </DialogHeader>
        <div>
          <CreateWorklogForm
            projectCode={projectCode}
            issueCode={issueCode}
            onSuccess={() => setOpen(false)}
          />
        </div>
      </DialogContent>
    </Dialog>
  );
}
