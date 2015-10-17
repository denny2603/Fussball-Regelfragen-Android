package de.simontenbeitel.regelfragen.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.activity.AssessedExamActivity;
import de.simontenbeitel.regelfragen.ui.adapter.AssessedQuestionListAdapter;

/**
 * @author Simon Tenbeitel
 */
public class AssessedQuestionListFragment extends ListFragment {

    public static final String QUESTION_LIST_EXTRA = "question_list";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(int position);
    }

    /**
     * Create a new instance of this fragment. It is recommended to only use this method to create a new instance.
     * The questions will be stored as arguments for the fragment.
     *
     * @param questions list of all questions. The list's implementation must implement Serializable
     * @return the new instance of AssessedQuestionListFragment with given questions stored as arguments
     */
    public static AssessedQuestionListFragment newInstance(List<Question> questions) {
        if (!(questions instanceof Serializable)) {
            throw new InvalidParameterException("The list of questions must be stored in a list which implements Serializable.");
        }

        Bundle args = new Bundle();
        args.putSerializable(QUESTION_LIST_EXTRA, (Serializable) questions);

        AssessedQuestionListFragment fragment = new AssessedQuestionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        List<Question> questions = (List<Question>) args.getSerializable(QUESTION_LIST_EXTRA);
        ListAdapter adapter = new AssessedQuestionListAdapter(getContext(), questions);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (null != savedInstanceState
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activities containing this fragment must implement its callbacks.
        if (!(context instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In two-pane mode, list items should be given the
        // 'activated' state when touched.
        AssessedExamActivity activity = (AssessedExamActivity) getActivity();
        setActivateOnItemClick(activity.isTwoPane());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        if (null != mCallbacks) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mCallbacks.onItemSelected(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (ListView.INVALID_POSITION == position) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

}
