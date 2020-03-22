package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Souei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souei.class).SetSkill(2, CardRarity.UNCOMMON);

    public Souei()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.TenSura);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.Callback(() ->
        {
            for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
            {
                PoisonPower poison = GameUtilities.GetPower(enemy, PoisonPower.class);
                if (poison != null)
                {
                    GameActions.Top.Callback(new PoisonLoseHpAction(enemy, player, poison.amount, AbstractGameAction.AttackEffect.POISON))
                    .AddCallback(GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID), (intangible, action) ->
                    {
                        if (GameUtilities.TriggerOnKill(action.target, true))
                        {
                            if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) == (int) intangible)
                            {
                                GameActions.Top.StackPower(new IntangiblePlayerPower(player, 1));
                            }
                        }
                    });
                }
            }
        });
    }
}