import type { Activity } from '../types';

type Props = {
  activities?: Activity[];
};

export default function ActivityTimeline({ activities = [] }: Props) {
  if (activities.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow p-4">
        <p className="text-gray-500 text-sm">No activity yet.</p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {activities.map((activity) => (
        <div key={activity.id} className="bg-white rounded-xl shadow p-4 border-l-4 border-blue-500" >
          <div className="flex justify-between items-center text-sm mb-1">
            <p className="font-semibold">{activity.activityType}</p>
            <p className="text-gray-500">
              {new Date(activity.createdAt).toLocaleString()}
            </p>
          </div>

          <p className="text-sm mb-1">{activity.description}</p>
          <p className="text-xs text-gray-500">By {activity.performedBy}</p>
        </div>
      ))}
    </div>

  );
}