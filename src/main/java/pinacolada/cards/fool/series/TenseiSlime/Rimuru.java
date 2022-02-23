package pinacolada.cards.fool.series.TenseiSlime;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import pinacolada.actions.special.RimuruAction;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCardTrait;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Rimuru extends FoolCard implements OnAfterCardPlayedSubscriber
{
    public static final PCLCardData DATA = Register(Rimuru.class)
            .SetSkill(-2, CardRarity.RARE, PCLCardTarget.AoE)
            .SetMaxCopies(2)
            .SetTraits(PCLCardTrait.Protagonist, PCLCardTrait.Demon)
            .SetSeriesFromClassPackage();

    public AbstractCard copy;

    public Rimuru()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1, 0, 0);
        SetVolatile(true);

        this.copy = this;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onAfterCardPlayed.Subscribe(this);
    }

    //@Formatter: Off
    @Override public final boolean canUpgrade() { return false; }
    @Override public final void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) { }
    @Override public final void upgrade() { }
    //@Formatter: On

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card != copy && !(card instanceof Rimuru) && !card.purgeOnUse && !card.isInAutoplay)
        {
            PCLActions.Top.Add(new RimuruAction(this, card));
        }
    }
}