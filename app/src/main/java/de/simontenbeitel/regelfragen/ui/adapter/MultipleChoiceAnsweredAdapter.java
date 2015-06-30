package de.simontenbeitel.regelfragen.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;

/**
 * An Adapter to map the answers of a multiple choice question to a list.
 * The chosen and correct/wrong answer will be displayed as an image.
 *
 * @author Simon Tenbeitel
 */
public class MultipleChoiceAnsweredAdapter extends ArrayAdapter<String> {

    private int mSolutionIndex;
    private int mChosenIndex;

    public MultipleChoiceAnsweredAdapter(Context context, String[] objects, int mSolutionIndex, int mChosenIndex) {
        super(context, R.layout.row_multiplechoiceanswer_solution, objects);
        this.mSolutionIndex = mSolutionIndex;
        this.mChosenIndex = mChosenIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (null == rowView) {
            rowView = View.inflate(getContext(), R.layout.row_multiplechoiceanswer_solution, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.answerText.setText(getItem(position));
        if (mSolutionIndex == position) {
            holder.correctImage.setImageResource(R.drawable.correct);
        } else if (mChosenIndex == position) {
            holder.correctImage.setImageResource(R.drawable.wrong);
        } else {
            holder.correctImage.setImageDrawable(null);
        }

        return rowView;
    }

    static class ViewHolder {

        @InjectView(R.id.correctImage) ImageView correctImage;
        @InjectView(R.id.answerText) TextView answerText;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

}
