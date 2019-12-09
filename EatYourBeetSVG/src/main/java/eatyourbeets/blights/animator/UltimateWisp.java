package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.cards.status.VoidCard;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnShuffleSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class UltimateWisp extends AnimatorBlight implements OnBattleStartSubscriber, OnShuffleSubscriber
{
    public static final String ID = CreateFullID(UltimateWisp.class.getSimpleName());

    public UltimateWisp()
    {
        super(ID, 1);

        this.counter = -1;
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
        GameActionsHelper_Legacy.MakeCardInDrawPile(new VoidCard(), initialAmount, false);
        this.flash();
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onShuffle.Subscribe(this);
    }
}