import { format } from "date-fns";
// import { Calendar as CalendarIcon } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/shadcnui/ui/button";
import { Calendar } from "@/shadcnui/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/shadcnui/ui/popover";
import { TimePicker } from "./TimePicker";

export type DateTimePickerProps = {
  date: Date;
  setDate: (_: any) => void;
  allowFutureDates: boolean;
};

export function DateTimePicker({
  date,
  setDate,
  allowFutureDates = false,
}: DateTimePickerProps) {
  // const [date, setDate] = React.useState<Date>();

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-[280px] justify-start text-left font-normal",
            !date && "text-muted-foreground"
          )}
        >
          {/* <CalendarIcon className="mr-2 h-4 w-4" /> */}
          {date ? format(date, "PPP HH:mm") : <span>Pick a date</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0">
        {allowFutureDates ? (
          <Calendar
            mode="single"
            selected={date}
            onSelect={setDate}
            initialFocus
          />
        ) : (
          <Calendar
            toDate={new Date()}
            mode="single"
            selected={date}
            onSelect={setDate}
            initialFocus
          />
        )}
        <div className="p-3 border-t border-border">
          <TimePicker setDate={setDate} date={date} />
        </div>
      </PopoverContent>
    </Popover>
  );
}
