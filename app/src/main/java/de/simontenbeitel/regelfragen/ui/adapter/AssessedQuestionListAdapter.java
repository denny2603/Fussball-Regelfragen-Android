package de.simontenbeitel.regelfragen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * Adapter to display a list of all questions in an assessed exam.
 *
 * @author Simon Tenbeitel
 */
public class AssessedQuestionListAdapter extends ArrayAdapter<Question> {

    public AssessedQuestionListAdapter(Context context,  List<Question> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();

        ViewHolder holder;
        if (null != convertView) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        String text = String.format(context.getString(R.string.questionWithNumber), (position + 1));
        holder.text1.setText(text);
        //todo Color text red if user chose the wrong answer

        return convertView;
    }

    static class ViewHolder {
        @Bind(android.R.id.text1) TextView text1;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
