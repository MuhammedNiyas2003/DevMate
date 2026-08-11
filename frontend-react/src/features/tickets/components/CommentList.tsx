import type { Comment } from '../types';

export default function CommentList({ comments }: { comments: Comment[] }) {
  return (
    <div className="space-y-4">
      {comments.length === 0 && (
        <p className="text-gray-500 text-sm">No comments yet.</p>
      )}

      {comments.map((comment) => (
        <div key={comment.id} className="bg-white rounded-xl shadow p-4">
          <div className="flex justify-between items-center text-sm mb-2">
            <p className="font-semibold">{comment.authorEmail}</p>
            <p className="text-gray-500">
              {new Date(comment.createdAt).toLocaleString()}
            </p>
          </div>

          <p className="whitespace-pre-wrap">{comment.comment}</p>
        </div>
      ))}
    </div>
  );
}