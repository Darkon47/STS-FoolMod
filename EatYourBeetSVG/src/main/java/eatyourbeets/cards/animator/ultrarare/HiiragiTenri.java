package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final String ID = Register(HiiragiTenri.class, EYBCardBadge.Exhaust);

    public HiiragiTenri()
    {
        super(ID, 4, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 20);
        SetUpgrade(0, 0, 10);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MoveCards(player.exhaustPile, player.drawPile);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.discardPile.group)
        {
            GameActions.Top.PlayCard(c, p.discardPile, m).SetExhaust(true);
        }

        GameActions.Top.GainTemporaryHP(this.magicNumber);
    }
}