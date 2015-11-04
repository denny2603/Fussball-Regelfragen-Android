package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;
import de.simontenbeitel.regelfragen.objects.MultipleChoiceQuestion;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class AnsweredQuestionFragment extends QuestionFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    public static AnsweredQuestionFragment newAnsweredFragment(Question question) {
        AnsweredQuestionFragment fragment;
        if (question instanceof GameSituationQuestion) {
            fragment = new GameSituationAnsweredFragment();
        } else if (question instanceof MultipleChoiceQuestion) {
            fragment = new MultipleChoiceAnsweredFragment();
        } else {
            throw new InvalidParameterException("No Fragment for question: " + question);
        }
        Bundle args = new Bundle();
        args.putSerializable(QuestionFragment.TAG_QUESTION_EXTRA, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void replaceAnswerContainer(View container, View answerView) {
        LinearLayout answerRootView = new LinearLayout(getActivity());
        answerRootView.setOrientation(LinearLayout.VERTICAL);

        TextView faultsText = new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin), 0, 0);
        faultsText.setLayoutParams(layoutParams);
        faultsText.setTextColor(getResources().getColor(R.color.abc_primary_text_material_light));
        faultsText.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        mQuestion.printFaults(faultsText);

        answerRootView.addView(answerView);
        answerRootView.addView(faultsText);

        super.replaceAnswerContainer(container, answerRootView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.assessed_question, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_explanation:
                Toast.makeText(getContext(), "TODO: Implement", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wrong_solution:
                Toast.makeText(getContext(), "TODO: Implement", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
