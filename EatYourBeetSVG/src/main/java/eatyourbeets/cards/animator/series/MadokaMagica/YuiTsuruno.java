package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class YuiTsuruno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(4, 0);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.MoveCards(p.drawPile, p.discardPile, 1)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Top)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                if (cards.get(0).type == CardType.ATTACK)
                {
                    GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
                }
                else
                {
                    GameActions.Bottom.ChannelOrb(new Fire(), true);
                }
            }
        });

        GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
    }
}