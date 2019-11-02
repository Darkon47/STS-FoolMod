package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.cards.status.VoidCard;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnShuffleSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class UltimateWisp extends AnimatorBlight implements OnBattleStartSubscriber, OnShuffleSubscriber
{
    public static final String ID = CreateFullID(UltimateWisp.class.getSimpleName());

    public UltimateWisp()
    {
        super(ID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        OnBattleStart();
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        GameActionsHelper.MakeCardInDrawPile(new VoidCard(), 1, false);
        this.flash();
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onShuffle.Subscribe(this);
    }
}